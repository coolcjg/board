package com.cjg.traveling.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.domain.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class JwtService {
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	UserService userService;
	
	public Map<String, Object> accessToken(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		String refreshToken = request.getHeader("refreshToken");
		
		System.out.println("accessToken");
		System.out.println("refreshToken : " + refreshToken);
		
		try {		
			if(jwt.validateJwtToken(refreshToken)) {
				
				String userId= jwt.getUserId(refreshToken);
				User user = userService.findByUserId(userId);
				
				if(user.getRefreshToken().equals(refreshToken)) {
					String accessToken = jwt.createAccessToken(user);
					result.put("code", HttpServletResponse.SC_OK);
					result.put("accessToken", accessToken);
				}
			}
		}catch(ExpiredJwtException e) {
			result.put("code", HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "ExpiredJwtException");			
		}catch(UnsupportedJwtException e) {
			result.put("code", HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "UnsupportedJwtException");
		}catch(MalformedJwtException e) {
			result.put("code", HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "MalformedJwtException");
		}catch(SignatureException e) {
			result.put("code", HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "SignatureException");
		}catch(IllegalArgumentException e) {
			result.put("code", HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "IllegalArgumentException");
		}
		
		return result;		
		
	}

}
