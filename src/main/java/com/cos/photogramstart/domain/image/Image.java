package com.cos.photogramstart.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity // DB에 테이블 생성
public class Image {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	private String caption;
	private String postImageUrl; // 사진을 전송받아서 그 사진을 특정 폴더에 저장 -> DB에 그 저장된 경로를 insert
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;
	
	// 이미지 좋아요
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Likes> likes;
	
	// DB에 칼럼이 만들어 지지 않는다.
	@Transient
	private boolean likeState;
	
	@Transient
	private int likeCount;
	
	// 이미지 댓글 -> lazy loading
	@JsonIgnoreProperties({"image", "comments"})
	@OneToMany(mappedBy = "image")
	private List<Comment> comments;
	
	private LocalDateTime createDate;
	
	@PrePersist
	private void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
