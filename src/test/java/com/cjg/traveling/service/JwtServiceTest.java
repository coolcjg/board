package com.cjg.traveling.service;

import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

	@InjectMocks
	Jwt jwt;

	@Mock
	Jwt jwtMock;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	UserService userService;

	@InjectMocks
	JwtService jwtService;
	
	@Test
	@DisplayName("accessToken 가져오기")
	public void accessToken(){
		
		User user = new User();
		user.setUserId("coolcjg");
		user.setName("최종규");

		String refreshToken = jwt.createRefreshToken(user);

		user.setRefreshToken(refreshToken);

		given(jwtMock.validateJwtToken(refreshToken)).willReturn(true);
		given(jwtMock.getUserId(refreshToken)).willReturn(user.getUserId());
		given(jwtMock.createAccessToken(user)).willReturn(jwt.createAccessToken(user));
		given(userService.findByUserId(user.getUserId())).willReturn(user);

		Map<String, Object> result = jwtService.accessToken(refreshToken);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
		Assertions.assertThat(result.get("accessToken")).isNotNull();
	}

}
