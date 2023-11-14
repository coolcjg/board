package com.cjg.traveling.exception;

import com.cjg.traveling.enums.ExceptionEnum;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
	
	private ExceptionEnum error;
	
	public ApiException(ExceptionEnum e) {
		super(e.getMessage());
		this.error = e;
	}

}
