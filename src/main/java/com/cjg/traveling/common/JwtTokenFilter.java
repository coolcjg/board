package com.cjg.traveling.common;

import java.io.IOException;

import com.cjg.traveling.common.response.Response;
import com.cjg.traveling.common.response.Result;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private Jwt jwt;

	/*
	인증 성공시 Authentication객체를 생성하여 SecurityContextHolder에 저장한다.
	이후 실행되는 UsernamePasswordAuthenticationFilter는 이미 인증이 완료된 상태이다.
	다음 필터로 계속 전달되면서 컨트롤러까지 요청이 지나간다.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException,  IOException{
		
		String token = ((HttpServletRequest) request).getHeader("accessToken");
		
		//유효한 토큰 확인
		try {
			if(token != null && jwt.validateJwtToken(token)) {
				
				String userId= jwt.getUserId(token);
				
				AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				securityContext.setAuthentication(authentication);
				SecurityContextHolder.setContext(securityContext);
			}else{
				//토큰이 없을 때
				Gson gson = new Gson();
				Response<String> responseDto = Response.fail(Result.UNAUTHORIZED);
				response.getWriter().print(gson.toJson(responseDto));
				return;
			}
			
		}catch(ExpiredJwtException e) {
			request.setAttribute("exception", "ExpiredJwtException");			
		}catch(UnsupportedJwtException e) {
			request.setAttribute("exception", "UnsupportedJwtException");
		}catch(MalformedJwtException e) {
			request.setAttribute("exception", "MalformedJwtException");
		}catch(SignatureException e) {
			request.setAttribute("exception", "SignatureException");
		}catch(IllegalArgumentException e) {
			request.setAttribute("exception", "IllegalArgumentException");
		}
		
		chain.doFilter(request, response);
	}
}
