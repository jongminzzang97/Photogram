package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드를 DI할 때 사용
@Controller // 1.Ioc 2. 파일을 return 하는 컨트롤러
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;
//	public AuthController(AuthService authService) {
//		this.authService = authService;
//	}

	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}

	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}

	// 회원가입버튼 -> /auth/signup -> /auth/signin -> 403이 뜬다 왜?? CSRF 토큰이 활성화 되어있기 때문에
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {
		User user = signupDto.toEntity();
		User userEntity = authService.회원가입(user);
		return "auth/signin";
	}
}
