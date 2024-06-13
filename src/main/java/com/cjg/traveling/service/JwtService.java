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
	
	public Map<String, Object> accessToken(String refreshToken){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {		
			if(jwt.validateJwtToken(refreshToken)) {
				
				String userId= jwt.getUserId(refreshToken);
				User user = userService.findByUserId(userId);
				
				if(user.getRefreshToken().equals(refreshToken)) {
					String accessToken = jwt.createAccessToken(user);
					result.put("message", "success");
					result.put("accessToken", accessToken);
				}
			}
		}catch(ExpiredJwtException e) {
			result.put("message", "ExpiredJwtException");			
		}catch(UnsupportedJwtException e) {
			result.put("message", "UnsupportedJwtException");
		}catch(MalformedJwtException e) {
			result.put("message", "MalformedJwtException");
		}catch(SignatureException e) {
			result.put("message", "SignatureException");
		}catch(IllegalArgumentException e) {
			result.put("message", "IllegalArgumentException");
		}
		
		return result;		
		
	}

}
