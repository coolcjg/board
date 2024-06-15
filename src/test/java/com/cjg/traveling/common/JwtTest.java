package com.cjg.traveling.common;


import com.cjg.traveling.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtTest {

    @InjectMocks
    Jwt jwt;

    @Test
    @DisplayName("accessToken 생성")
    public void createAccessToken(){
        User user = new User();
        user.setUserId("coolcjg");
        user.setName("최종규");

        String result = jwt.createAccessToken(user);
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("refreshToken 생성")
    public void createRefreshToken(){
        User user = new User();

        user.setUserId("coolcjg");
        user.setName("최종규");

        String result = jwt.createRefreshToken(user);
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("토큰 확인")
    public void validateJwtToken(){
        User user = new User();
        user.setUserId("coolcjg");
        user.setName("최종규");

        String token = jwt.createRefreshToken(user);
        boolean result = jwt.validateJwtToken(token);

        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("아이디 확인")
    public void getUserId(){
        User user = new User();
        String userId = "coolcjg";
        user.setUserId(userId);
        user.setName("최종규");

        String token = jwt.createRefreshToken(user);
        String result = jwt.getUserId(token);

        Assertions.assertThat(result).isEqualTo(userId);
    }
}
