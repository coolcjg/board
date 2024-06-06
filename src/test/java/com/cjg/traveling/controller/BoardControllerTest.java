package com.cjg.traveling.controller;


import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.config.SecurityConfig;
import com.cjg.traveling.service.BoardService;
import com.cjg.traveling.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = BoardController.class
        , includeFilters={
            @ComponentScan.Filter(
                    type= FilterType.ASSIGNABLE_TYPE,
                    classes = SecurityConfig.class
            )
        }

)
public class BoardControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    Jwt jwt;

    @MockBean
    UserService userService;

    @MockBean
    BoardService boardService;

    @Test
    @DisplayName("리스트 테스트")
    void list() throws Exception{

        //파라미터
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
        mvm.add("pageNumber", "1");

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        given(boardService.list(mvm.toSingleValueMap())).willReturn(result);

        mvc.perform(get("/board/list").params(mvm)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }


}
