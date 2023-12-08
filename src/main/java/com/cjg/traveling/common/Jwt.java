package com.cjg.traveling.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class Jwt {
	
	// JWT 비밀키
	private static final String SECRET_KEY = "ChopinBlackKeyChopinBlackKeyChopinBlackKeyChopinBlackKey";
	
	private Logger logger = LoggerFactory.getLogger(Jwt.class);

	
	/*
	public static void main(String[] args){
		
		Map<String, String> param = new HashMap();
		param.put("id", "sampleId");
		param.put("name", "sampleName");
		
		String token = createAccessToken(param);
		System.out.println("JWT Token : " + token);
		
		// 생성된 JWT토큰 검증
		boolean isValid = validateJwtToken(token);
		System.out.println("JWT Token Validation : " + isValid);
		
	}
	*/
	
	// JWT 토큰 생성
	public String createAccessToken(Map<String, String> param) {
		
		// Header
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		// Payload
		Map<String, Object> payloads = new HashMap();
		
		for(String key : param.keySet()){
			payloads.put(key, param.get(key));
		}
		
		String token = Jwts.builder()
				.setClaims(payloads)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.compact();
		
		
		return token;
	}
	
	// JWT 리프레시 토큰 생성
	public String createRefreshToken(Map<String, String> param) {
		
		// Header
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		// Payload
		Map<String, Object> payloads = new HashMap();
		
		for(String key : param.keySet()){
			payloads.put(key, param.get(key));
		}
		
		String token = Jwts.builder()
				.setClaims(payloads)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 604800000))
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.compact();
		
		
		return token;
	}	
		
	public boolean validateJwtToken(String token) {
		
		try {			
			Jws<Claims> claims = Jwts.parserBuilder()
									.setSigningKey(SECRET_KEY.getBytes())
									.build()
									.parseClaimsJws(token);
			
			boolean result = !claims.getBody().getExpiration().before(new Date());
			
			return result;
			
		}catch(Exception e) {
			logger.info("Error : " + e);
			return false;
		}
	}
	
	public String getUserId(String token) {
		
		Jws<Claims> claims = Jwts.parserBuilder()
								.setSigningKey(SECRET_KEY.getBytes())
								.build()
								.parseClaimsJws(token);
		
		return claims.getBody().get("id").toString();
		
	}
	

	
	
	

}
