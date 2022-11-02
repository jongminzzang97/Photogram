package com.cos.photogramstart.domain.User;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA - Java Persistence API : 자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity // DB에 테이블 생성
public class User {
		
		@Id //primary key는 필수임
		@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라간다.
		private int id;
		
		@Column(length=100, unique= true)
		private String username;
		@Column(nullable=false)
		private String password;
		@Column(nullable=false)
		private String name;
		private String website; // 웹사이트
		private String bio; // 자기소개
		@Column(nullable=false)
		private String email;
		private String phone;
		private String gender;
		
		private String profileImageUrl; // 사진
		private String role; // 권한
		
		// 나는 연관관계의 주인이 아니다. -> 테이블에 칼럼을 만들지 않음
		// User를 select할 때 해당 User id로 등록된 image들을 가져올 수 있도록 함
		// LAZY = select할 때 해당 User Id로 등록된 image들을 가져오지마 - 대신 getImages() 함수가 호출 될 때 가져올수 있도록
		// Eager = select할 때 해당 User Id로 등록된 image들을 join해서 바로 가져와
		@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
		@JsonIgnoreProperties("user") // user를 응답할때 image를 파싱하는데, 그때 image 내부에 있는 user를 파싱하지 말라는 뜻
		private List<Image> images;
		
		private LocalDateTime createDate;
		
		@PrePersist // DB Insert 직전에 실행
		private void createDate() {
			this.createDate = LocalDateTime.now();
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name
					+ ", website=" + website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender="
					+ gender + ", profileImageUrl=" + profileImageUrl + ", role=" + role
					+ ", createDate=" + createDate + "]";
		}
		
}
