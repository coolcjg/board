package com.cjg.traveling.controller;

import com.cjg.traveling.common.response.Response;
import com.cjg.traveling.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwtController {

	private final JwtService jwtService;
	
	@GetMapping("/jwt/accessToken")
	public Map<String, Object> accessToken(HttpServletRequest request) {
		String refreshToken = request.getHeader("refreshToken");
		return jwtService.accessToken(refreshToken);
	}

	@GetMapping("/jwt/{userId}")
	public ResponseEntity<Response<String>> accessTokenForTest(HttpServletRequest request, @PathVariable("userId") String userId) {
		return jwtService.accessTokenForTest(userId);
	}

}
