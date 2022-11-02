package com.cos.photogramstart.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.User.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // 원래 로그인 때 실행되는 UserDetailsService를 내가 구현한 PrincipalDetailsService가 동작할 수 있도록 컨테이너에 등록
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	// 1. 패스워드는 알아서 체킹하니까 신경쓸 필요 없다
	// 2. 리턴이 잘 되면 자동으로 UserDetails 세션을 만든다.
	 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userEntity = userRepository.findByUsername(username);
		if (userEntity == null) {
			return null;
		}
		else {
			return new PrincipalDetails(userEntity);
		}
	}
	
}
