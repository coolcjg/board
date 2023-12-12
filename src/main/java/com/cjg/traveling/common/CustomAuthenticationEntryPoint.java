package com.cjg.traveling.common;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		System.out.println("aaaa");
		
		String exception = (String)request.getAttribute("exception");
		
		System.out.println("exception : " + exception);
		
		
		if(exception != null) {
			setResponse(response, "1");
		}
		
		
	}
	
	private void setResponse(HttpServletResponse response, String message) throws IOException {
		
		System.out.println("aaaaaaaaa여 기 탐");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		Gson gson = new Gson();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("status", "200");
		
		response.getWriter().print(gson.toJson(jsonObject));
		
	}	
	
	
	

}
