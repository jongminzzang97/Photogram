package com.cos.photogramstart.web.dto.image;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.image.Image;

import lombok.Data;

@Data
public class ImageUploadDto {
	// @NotBlank  : MultipartFile 타입에 대해서는 지원하지 않음
	private MultipartFile file;
	private String caption;
	
	public Image toEntity(String postImageUrl, User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.user(user)
				.build();
	}
}
