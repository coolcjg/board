package com.cjg.traveling.controller;


import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.config.SecurityConfig;
import com.cjg.traveling.dto.UserDto;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = UserController.class
    , includeFilters={
    @ComponentScan.Filter(
            type= FilterType.ASSIGNABLE_TYPE,
            classes = SecurityConfig.class
    )
    }
)
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    Jwt jwt;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 존재 확인")
    void existsById() throws Exception{

        String userId = "coolcjg";

        Map<String,Object> result = new HashMap<>();
        result.put("count", 1);
        result.put("message", "success");

        given(userService.existsById(userId)).willReturn(result);
        mvc.perform(get("/user/count/").param("userId", userId))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @WithMockUser
    @DisplayName("유저 리스트 확인")
    void list() throws Exception{

        UserDto user = new UserDto();
        user.setPageNumber(1);
        user.setPageSize(10);

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        Map<String, Object> data = new HashMap<String, Object>();
        result.put("data", data);

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
        mvm.add("pageNumber", "1");
        mvm.add("pageSize", "10");

        given(userService.list(user)).willReturn(result);

        mvc.perform(get("/user/list").params(mvm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }


    @Test
    @WithMockUser
    @DisplayName("사용자 등록")
    void insertUser() throws Exception{

        UserDto userDto = new UserDto();
        userDto.setUserId("coolcjg");
        userDto.setPassword("1234");
        userDto.setName("최종규");
        userDto.setAuth("admin");

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        given(userService.insertUser(userDto)).willReturn(result);

        mvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                    )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("success"))
           .andDo(print());
    }


    @Test
    @WithMockUser
    @DisplayName("사용자 수정")
    void putUser() throws Exception{

        UserDto userDto = new UserDto();
        userDto.setUserId("coolcjg");
        userDto.setPassword("1234");

        Map<String ,Object> result = new HashMap<String, Object>();
        result.put("message", "success");

        given(userService.putUser(userDto)).willReturn(result);

        mvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }


    @Test
    @WithMockUser
    @DisplayName("사용자 정보 가져오기")
    void user() throws Exception{

        String userId = "coolcjg";

        Map<String,Object> result = new HashMap<>();
        UserDto userDto = new UserDto();
        result.put("data", userDto);
        result.put("message", "success");

        given(userService.user(userId)).willReturn(result);

        mvc.perform(get("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andDo(print());
    }


    @Test
    @WithMockUser
    @DisplayName("로그인")
    void login() throws Exception{

        UserDto userDto = new UserDto();
        userDto.setUserId("coolcjg");
        userDto.setPassword("1234");

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        given(userService.login(userDto)).willReturn(result);

        mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("삭제")
    void deleteUser() throws Exception{

        UserDto userDto = new UserDto();
        userDto.setUserId("coolcjg");
        userDto.setPassword("1234");

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        given(userService.delete(userDto)).willReturn(result);

        mvc.perform(delete("/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("success"))
            .andDo(print());

    }
}
