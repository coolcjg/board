package com.cjg.traveling.controller;

import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.config.SecurityConfig;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.service.ApiService;
import com.cjg.traveling.service.JwtService;
import com.cjg.traveling.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = JwtController.class
        , includeFilters={
        @ComponentScan.Filter(
                type= FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
}
)
public class JwtControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    Jwt jwt;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserService userService;

    @MockBean
    ApiService apiService;

    @Test
    @WithMockUser
    @DisplayName("accessToken 가져오기")
    void accessToken() throws Exception{

        User user = new User();
        user.setUserId("coolcjg");
        user.setName("최종규");
        String refreshToken = jwt.createRefreshToken(user);

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        given(jwtService.accessToken(refreshToken)).willReturn(result);

        mvc.perform(get("/jwt/accessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }
}
