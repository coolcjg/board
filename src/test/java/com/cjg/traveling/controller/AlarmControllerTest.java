package com.cjg.traveling.controller;


import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.config.SecurityConfig;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.service.AlarmService;
import com.cjg.traveling.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
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
        controllers = AlarmController.class
        , includeFilters={
        @ComponentScan.Filter(
                type= FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
})
public class AlarmControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    Jwt jwt;

    @MockBean
    AlarmService alarmService;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("알람 리스트")
    void list() throws Exception{

        String userId = "coolcjg";
        User user = new User();
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwt.createAccessToken(user);

        Map<String, Object> map = new HashMap();
        map.put("accessTokenUserId", userId);
        map.put("userId", userId);
        map.put("pageNumber", "1");

        Map<String, Object> result = new HashMap();
        result.put("message", "success");

        given(jwt.getUserId(accessToken)).willReturn(userId);
        given(alarmService.list(map)).willReturn(result);

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap();
        for(String key : map.keySet()){
            mvm.add(key, (String)map.get(key));
        }

        mvc.perform(get("/alarm/list")
                .params(mvm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("알람 삭제")
    void deleteAlarm() throws Exception{

        String userId = "coolcjg";
        Long alarmId = 2L;

        User user = new User();
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwt.createAccessToken(user);

        Map<String, Object> result = new HashMap();
        result.put("message", "success");

        given(jwt.getUserId(accessToken)).willReturn(userId);
        given(alarmService.delete(alarmId, userId)).willReturn(result);

        mvc.perform(delete("/alarm")
                        .param("alarmId", String.valueOf(alarmId))
                    )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("알람 체크")
    void check() throws Exception{

        String userId = "coolcjg";
        Long alarmId = 2L;

        User user = new User();
        user.setUserId(userId);
        user.setName("최종규");
        String accessToken = jwt.createAccessToken(user);

        Map<String, Object> result = new HashMap();
        result.put("message", "success");

        given(jwt.getUserId(accessToken)).willReturn(userId);
        given(alarmService.check(userId, alarmId)).willReturn(result);

        mvc.perform(put("/alarm/check/" + alarmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }
}
