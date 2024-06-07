package com.cjg.traveling.controller;


import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.config.SecurityConfig;
import com.cjg.traveling.dto.MediaDto;
import com.cjg.traveling.service.MediaService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = MediaController.class
    , includeFilters={
        @ComponentScan.Filter(
                type= FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
    }
)
public class MediaControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    Jwt jwt;

    @MockBean
    MediaService mediaService;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("미디어 가져오기")
    void getMedia() throws Exception{

        Long mediaId = 1L;

        MediaDto mediaDto = new MediaDto();

        Map<String,Object> result = new HashMap<>();
        result.put("data", mediaDto);
        result.put("message", "success");

        given(mediaService.getMedia(mediaId)).willReturn(result);

        mvc.perform(get("/media/"+mediaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("미디어 삭제")
    void deleteMedia() throws Exception{

        Long mediaId = 1L;

        MediaDto mediaDto = new MediaDto();

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        given(mediaService.deleteMedia(mediaId)).willReturn(result);

        mvc.perform(delete("/media/"+mediaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }
}
