package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.User.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
	
	private boolean pageOwnerState;
	private int imageCount;
	private boolean subscribeState;
	private int followerCount;
	private int followingCount;
	private User user;
}
