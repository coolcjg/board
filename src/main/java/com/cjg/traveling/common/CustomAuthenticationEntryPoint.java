package com.cjg.traveling.common;

import com.cjg.traveling.common.response.Response;
import com.cjg.traveling.common.response.Result;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

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
		Response<Void> responseDto = Response.fail(Result.UNAUTHORIZED);
		response.getWriter().print(gson.toJson(responseDto));
	}
	
}
