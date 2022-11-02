package com.cos.photogramstart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.User.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // 1. Ioc 2. 트랜젝션 관리
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional // Write(Insert, Delete, Update)
	public User 회원가입(User user) {
		// 회원 가입을 진행 -> repository가 필요
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole("ROLE_USER");
		User userEntity = userRepository.save(user);
		return userEntity;
	}
}
