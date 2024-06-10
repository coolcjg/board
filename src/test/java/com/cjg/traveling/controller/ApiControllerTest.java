package com.cjg.traveling.controller;


import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.config.SecurityConfig;
import com.cjg.traveling.dto.MediaDto;
import com.cjg.traveling.service.ApiService;
import com.cjg.traveling.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ApiController.class
        , includeFilters={
        @ComponentScan.Filter(
                type= FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
}
)
public class ApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    Jwt jwt;

    @MockBean
    ApiService apiService;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("인코딩 결과 수신")
    void encodingResult() throws Exception{

        MediaDto mediaDto = new MediaDto();

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        given(apiService.encodingResult(mediaDto)).willReturn(result);

        mvc.perform(post("/api/encodingResult")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mediaDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

}
