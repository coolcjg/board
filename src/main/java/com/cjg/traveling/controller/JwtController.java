package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class JwtController {
	
	@Autowired
	JwtService jwtService;
	
	@GetMapping("/jwt/accessToken")
	public Map<String, Object> accessToken(HttpServletRequest request) {
		String refreshToken = request.getHeader("refreshToken");
		return jwtService.accessToken(refreshToken);
	}

}
