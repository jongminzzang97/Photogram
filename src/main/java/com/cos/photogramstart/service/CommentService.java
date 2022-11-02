package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.User.UserRepository;
import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	//		@Transactional
	//		public Comment 댓글쓰기(String content, int iamgeId, int uesrId) {
	//			return commentRepository.mSave(content, iamgeId, uesrId);
	//		}
	
	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int uesrId) {
		
		// Tip 가짜 객체를 만든다. -> 어차피 comment DB에 들어가는 값은  imageId와 userId 이다.
		// findById 오래걸릴수 있음
		// 대신 return 시에 image와 user의 가짜데이터를 제공 받게됨.
		Image image = new Image();
		image.setId(imageId);
		
		//		User user = new User();
		//		user.setId(uesrId);
		// 	만약 제대로된 유저를 가지고 오고 싶다면..
		User userEntity = userRepository.findById(uesrId).orElseThrow(()->{
			throw new CustomApiException("유저아이디를 찾을 수 없습니다.");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);

		return commentRepository.save(comment);
	}
	
	@Transactional
	public void 댓글삭제(int id) {
		// 터질 수 있음 수정 필
		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
	}
}
