package com.cjg.traveling.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationEntryPointTest {

    @InjectMocks
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Test
    @DisplayName("commence")
    public void commence() throws Exception{

        HttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("exception", "에러");

        HttpServletResponse response = new MockHttpServletResponse();

        AuthenticationException ae = new AccountExpiredException("에러");

        customAuthenticationEntryPoint.commence(request, response, ae);
    }
}
