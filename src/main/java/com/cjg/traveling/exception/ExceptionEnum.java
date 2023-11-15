package com.cjg.traveling.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ExceptionEnum {
	//STATUS, CODE, MESSAGE
	RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),
	ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003"),
	PARAM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0004", " PARAM ERROR"),
	SECURITY_01(HttpStatus.UNAUTHORIZED, "S0001", " 권한이 없습니다.");
	
	private final HttpStatus status;
	private final String code;
	private String message;
	
	ExceptionEnum(HttpStatus status, String code){
		this.status = status;
		this.code = code;
	}
	
	ExceptionEnum(HttpStatus status, String code, String message){
		this.status = status;
		this.code = code;
		this.message = message;
	}

}
