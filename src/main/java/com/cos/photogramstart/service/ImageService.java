package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;

	@Value("${file.path}")
	private String uploadFolder;  // C:/workspace/springbootwork/upload/
	
	@Transactional(readOnly = true) // 영속성컨텍스트 변경을 감지해서 더티체킹 flush(반영) -> readonly일 때는 하지 않음
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// images에 좋아요 상태 답
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) { // 해당 이미지를 좋아요 한 사람들중 내가 있는지를 확인하는 과정
					image.setLikeState(true);
				}
			});
		});
		
		return images;
	}
	
	
	@Transactional
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); // 1.jpg 
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		// 통신, IO일어날 때 -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// image 테이블에 저장
		// imageUploadDto를 Image 객체로 변환하는 방법이 필요함 ->
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
		Image imageEntity = imageRepository.save(image);
		
		// System.out.println(imageEntity);
	}
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진() {
		
		return imageRepository.mPopular();
	}
	
	@Transactional
	public void 이미지삭제(int imageId, int principalId) {
		Image image = imageRepository.findById(imageId).orElseThrow(() -> {throw new CustomApiException("존재하지 않는 이미지 입니다.");});
		if (principalId == image.getUser().getId()) {
			try {
				imageRepository.delete(image);
			} catch (Exception e) {
				throw new CustomApiException(e.getMessage());
			}
		}
		else {
			throw new CustomApiException("해당 게시물의 주인이 아닙니다.");
		}
	}
}
