package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.User.UserRepository;
import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final SubscribeRepository subscribeRepository;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile) {
		
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid+"_"+profileImageFile.getOriginalFilename(); // 1.jpg 
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		// 통신, IO일어날 때 -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});
		
		userEntity.setProfileImageUrl(imageFileName);
		return userEntity;
	} // 더티체킹으로 업데이트됨
	
	@Transactional(readOnly = true)
	public UserProfileDto 회원프로필(int pageUserid, int principalId) {
		// SELECT * FROM image WHERE userID = :userId
		// 쿼리로 한다면 위와같이 하면 된다.
		// JPA를 이용해서 할 것
		
		UserProfileDto dto = new UserProfileDto();
		User userEntity = userRepository.findById(pageUserid).orElseThrow(() -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지 입니다.");
		});
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserid==principalId? true : false); 
		dto.setImageCount(userEntity.getImages().size());
		
		int subscribeState = subscribeRepository.mSubscripbeState(principalId, pageUserid);
		int followerCount = subscribeRepository.mfollowerCount(pageUserid);
		int followingCount = subscribeRepository.mfollowingCount(pageUserid);

		dto.setSubscribeState(subscribeState == 1);
		dto.setFollowerCount(followerCount);
		dto.setFollowingCount(followingCount);
		
		// 좋아요 카운트 추가
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikes().size());
			System.out.println(image.getLikes().size());
		});
		
		
		return dto;
	}
	
	@Transactional
	public User 회원수정(int id, User user) {
		// 1. 영속화
		User userEntity = userRepository.findById(id).orElseThrow(() -> {
				return new CustomValidationApiException("찾을 수 없는 Id입니다.");
		}); 
		
		
		// 2. 영속화된 오브젝트를 수정 - 더티체킹 (업데이트 완료)
		userEntity.setName(user.getName());
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		userEntity.setPassword(encPassword);
		userEntity.setWebsite(user.getWebsite());
		userEntity.setBio(user.getBio());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		
		return userEntity;
	} // 더티체킹이 일어남
	
}
