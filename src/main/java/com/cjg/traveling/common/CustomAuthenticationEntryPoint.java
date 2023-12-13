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
		
		String exception = (String)request.getAttribute("exception");
		
		if(exception != null) {
			setResponse(response, exception);
		}
		
	}
	
	private void setResponse(HttpServletResponse response, String message) throws IOException {
		
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("status", HttpServletResponse.SC_UNAUTHORIZED);
		jsonObject.addProperty("message", message);
		response.getWriter().print(gson.toJson(jsonObject));
	}
	
}
