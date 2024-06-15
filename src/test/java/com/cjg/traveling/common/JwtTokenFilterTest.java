package com.cjg.traveling.common;


import com.cjg.traveling.domain.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class JwtTokenFilterTest {

    @InjectMocks
    Jwt jwtInject;

    @Mock
    Jwt jwtMock;

    @InjectMocks
    JwtTokenFilter jwtTokenFilter;

    @Test
    @DisplayName("필터 테스트")
    public void doFilterInternal() throws Exception{

        MockHttpServletRequest request = new MockHttpServletRequest();

        User user = new User();
        String userId = "coolcjg";
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwtInject.createAccessToken(user);

        request.addHeader("accessToken", accessToken);

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = new MockFilterChain();

        given(jwtMock.validateJwtToken(accessToken)).willReturn(true);
        given(jwtMock.getUserId(accessToken)).willReturn(userId);


        jwtTokenFilter.doFilterInternal(request, response, chain);

        Assertions.assertThat(request.getAttribute("exception")).isNull();
    }

    @Test
    @DisplayName("필터 테스트 : ExpiredJwtException")
    public void doFilterInternal_ExpiredJwtException() throws Exception{

        MockHttpServletRequest request = new MockHttpServletRequest();

        User user = new User();
        String userId = "coolcjg";
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwtInject.createAccessToken(user);

        request.addHeader("accessToken", accessToken);

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = new MockFilterChain();

        given(jwtMock.validateJwtToken(accessToken)).willThrow(ExpiredJwtException.class);

        jwtTokenFilter.doFilterInternal(request, response, chain);
        Assertions.assertThat(request.getAttribute("exception")).isEqualTo("ExpiredJwtException");
    }

    @Test
    @DisplayName("필터 테스트 : UnsupportedJwtException")
    public void doFilterInternal_UnsupportedJwtException() throws Exception{

        MockHttpServletRequest request = new MockHttpServletRequest();

        User user = new User();
        String userId = "coolcjg";
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwtInject.createAccessToken(user);

        request.addHeader("accessToken", accessToken);

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = new MockFilterChain();

        given(jwtMock.validateJwtToken(accessToken)).willThrow(UnsupportedJwtException.class);

        jwtTokenFilter.doFilterInternal(request, response, chain);
        Assertions.assertThat(request.getAttribute("exception")).isEqualTo("UnsupportedJwtException");
    }

    @Test
    @DisplayName("필터 테스트 : MalformedJwtException")
    public void doFilterInternal_MalformedJwtException() throws Exception{

        MockHttpServletRequest request = new MockHttpServletRequest();

        User user = new User();
        String userId = "coolcjg";
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwtInject.createAccessToken(user);

        request.addHeader("accessToken", accessToken);

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = new MockFilterChain();

        given(jwtMock.validateJwtToken(accessToken)).willThrow(MalformedJwtException.class);

        jwtTokenFilter.doFilterInternal(request, response, chain);
        Assertions.assertThat(request.getAttribute("exception")).isEqualTo("MalformedJwtException");
    }

    @Test
    @DisplayName("필터 테스트 : SignatureException")
    public void doFilterInternal_SignatureException() throws Exception{

        MockHttpServletRequest request = new MockHttpServletRequest();

        User user = new User();
        String userId = "coolcjg";
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwtInject.createAccessToken(user);

        request.addHeader("accessToken", accessToken);

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = new MockFilterChain();

        given(jwtMock.validateJwtToken(accessToken)).willThrow(SignatureException.class);
        jwtTokenFilter.doFilterInternal(request, response, chain);
        Assertions.assertThat(request.getAttribute("exception")).isEqualTo("SignatureException");
    }

    @Test
    @DisplayName("필터 테스트 : IllegalArgumentException")
    public void doFilterInternal_IllegalArgumentException() throws Exception{

        MockHttpServletRequest request = new MockHttpServletRequest();

        User user = new User();
        String userId = "coolcjg";
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwtInject.createAccessToken(user);

        request.addHeader("accessToken", accessToken);

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = new MockFilterChain();

        given(jwtMock.validateJwtToken(accessToken)).willThrow(IllegalArgumentException.class);
        jwtTokenFilter.doFilterInternal(request, response, chain);
        Assertions.assertThat(request.getAttribute("exception")).isEqualTo("IllegalArgumentException");
    }
}
