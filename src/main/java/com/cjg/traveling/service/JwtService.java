package com.cjg.traveling.service;

import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.response.Response;
import com.cjg.traveling.domain.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final Jwt jwt;
	private final UserService userService;
	
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

	public ResponseEntity<Response<String>> accessTokenForTest(String userId){
		User user = userService.findByUserId(userId);
		return ResponseEntity.ok(Response.success(jwt.createAccessToken(user)));
	}

}
