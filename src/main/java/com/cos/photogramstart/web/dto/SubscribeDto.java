package com.cos.photogramstart.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {
	private int userId;
	private String userName;
	private String profileImageUrl;
	private Integer subscribeState;
	private Integer equalUserState;
}
