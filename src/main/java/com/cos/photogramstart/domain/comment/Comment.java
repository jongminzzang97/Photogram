package com.cos.photogramstart.domain.comment;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.likes.Likes;
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
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 100, nullable = false)
	private String content;

	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@JoinColumn(name = "imageId")
	@ManyToOne(fetch = FetchType.EAGER)
	private Image image;
	
	private LocalDateTime createDate;

	@PrePersist
	private void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
