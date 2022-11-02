package com.cos.photogramstart.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {
		
		// CMResponseDto, Script 비교
		// 1. 클라이언트가 응답받을 때는 Script가 좋음
		// 2. Ajax 통신 - CMResponseDto
		// 3. Android 통신 - CMResponseDto
		if (e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		}else {
			return Script.back(e.getErrorMap().toString());
		}
	}
	
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<CMRespDto<?>> validationApiException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<CMRespDto<?>> ApiException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);	
	}
	
	@ExceptionHandler(CustomException.class)
	public String Exception(CustomException e) {
		 return Script.back(e.getMessage());
	}
}
