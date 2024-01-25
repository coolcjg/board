package com.cjg.traveling.service;

import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
	
	@Mock
	Jwt jwt;
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserService userService;	
	
	@Test
	public void accessToken(){
		
		User userMock1 = new User();
		userMock1.setUserId("coolcjg");
		userMock1.setName("최종규");
		userMock1.setRefreshToken("eyJhbGciOiJIUzM4NCJ9.eyJuYW1lIjoi7LWc7KKF6recIiwiaWQiOiJjb29sY2pnIiwiaWF0IjoxNzA2MDg0NDk3LCJleHAiOjE3MDg3NjI4OTd9.SsGyUqiZBAzCKvScLE30zx-6B2Znyb1iwdmJ8g2X_rsqChAnir1xcbO7j6V5YYZu");
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		User[] refreshTokenArray =  {userMock1, userMock1};
		boolean[] validateJwtTokenArray =  {true, false};
		
		for(int i=0; i<refreshTokenArray.length; i++) {
			
			User userTemp = refreshTokenArray[i];
			
			String refreshToken = userTemp.getRefreshToken();
			
			given(jwt.validateJwtToken(refreshToken)).willReturn(validateJwtTokenArray[i]);
			try {		
				if(jwt.validateJwtToken(refreshToken)) {
					given(jwt.getUserId(refreshToken)).willReturn(userTemp.getUserId());
					String userId= jwt.getUserId(refreshToken);
					
					given(userService.findByUserId(userId)).willReturn(userTemp);
					User user = userService.findByUserId(userId);
					
					if(user.getRefreshToken().equals(refreshToken)) {
						
						given(jwt.createAccessToken(user)).willReturn(user.getRefreshToken());
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
		}
		
	}

}
