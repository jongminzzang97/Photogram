package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component // RestController, Service  모든것들이 Component를 상속해서 만들어져 있음
@Aspect
public class ValidationAdvice {
	
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {	
		System.out.println("web api 컨트롤러 ==========================================");
		
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg:args) {
			if(arg instanceof BindingResult) {
				// 다운케스팅
				BindingResult bindingResult = (BindingResult) arg;
				
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println(error.getDefaultMessage());
					}
				}
			}
		}

		return proceedingJoinPoint.proceed(); // profile 함수 실행
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("web 컨트롤러 ==========================================");
		
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg:args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;

				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println(error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성검사 실패함", errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
}
