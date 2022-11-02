package com.cos.photogramstart.domain.subscribe;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.User.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity // DB에 테이블 생성
@Table(
		uniqueConstraints = {
				@UniqueConstraint(
						name="subscribe_uk",
						columnNames = {"fromUserId", "toUserId"}
				)
		}
)
public class Subscribe {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	@JoinColumn(name="fromUserId")
	@ManyToOne
	private User fromUser;
	
	@JoinColumn(name="toUserId")
	@ManyToOne
	private User toUser;
	
	
	
	private LocalDateTime createDate;
	
	@PrePersist
	private void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
