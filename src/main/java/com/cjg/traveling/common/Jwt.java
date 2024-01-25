package com.cjg.traveling.common;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cjg.traveling.domain.User;
import com.cjg.traveling.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class Jwt {
	
	// JWT 비밀키
	private static final String SECRET_KEY = "ChopinBlackKeyChopinBlackKeyChopinBlackKeyChopinBlackKey";
	
	private Logger logger = LoggerFactory.getLogger(Jwt.class);
	
	@Autowired
	private UserRepository userRepository;	

	
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
	public String createAccessToken(User user) {
		
		// Header
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		// Payload
		Map<String, Object> payloads = new HashMap();
		payloads.put("id", user.getUserId());
		payloads.put("name", user.getName());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, 30);		
		
		String token = Jwts.builder()
				.setClaims(payloads)
				.setIssuedAt(new Date())
				.setExpiration(new Date(cal.getTimeInMillis()))
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.compact();
		
		
		return token;
	}
	
	// JWT 리프레시 토큰 생성
	public String createRefreshToken(User user) {
		
		// Header
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		// Payload
		Map<String, Object> payloads = new HashMap();
		payloads.put("id", user.getUserId());
		payloads.put("name", user.getName());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		
		System.out.println(cal.getTime());
		
		//30DAYS
		String token = Jwts.builder()
				.setClaims(payloads)
				.setIssuedAt(new Date())
				.setExpiration(new Date(cal.getTimeInMillis()))
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.compact();
		
		
		return token;
	}	
		
	public boolean validateJwtToken(String token) {
		
		System.out.println("aaddd");
		
		
		Jws<Claims> claims = Jwts.parserBuilder()
				.setSigningKey(SECRET_KEY.getBytes())
				.build()
				.parseClaimsJws(token);
		return true;
	}
	

	
	
	
	public String getUserId(String token) {
		
		Jws<Claims> claims = Jwts.parserBuilder()
								.setSigningKey(SECRET_KEY.getBytes())
								.build()
								.parseClaimsJws(token);
		
		return claims.getBody().get("id").toString();
		
	}
	

	
	
	

}
