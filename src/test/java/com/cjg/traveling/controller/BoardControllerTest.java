package com.cjg.traveling.controller;


import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.PageUtil;
import com.cjg.traveling.config.SecurityConfig;
import com.cjg.traveling.dto.BoardDto;
import com.cjg.traveling.dto.BoardSearchDto;
import com.cjg.traveling.dto.OpinionDto;
import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.repository.BoardRepository;
import com.cjg.traveling.service.BoardService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @MockBean
    BoardRepository boardRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 리스트")
    void list() throws Exception{

        //파라미터
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
        mvm.add("pageNumber", "1");
        mvm.add("searchType", "region");
        mvm.add("searchText", "서울");

        BoardSearchDto boardSearchDto = new BoardSearchDto();
        boardSearchDto.setPageNumber(1);
        boardSearchDto.setSearchType("region");
        boardSearchDto.setSearchText("서울");

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        List<BoardDto> boardList = new ArrayList();
        for(int i=0; i<1; i++){
            BoardDto temp = new BoardDto();

            temp.setBoardId(Long.valueOf(i));
            temp.setContents("내용"+i);
            temp.setRegDate(LocalDateTime.now());
            temp.setRegion("서울");
            temp.setTitle("제목"+i);
            temp.setView(i);

            UserDto userDTO = new UserDto();
            userDTO.setName("이름"+i);
            userDTO.setUserId("cjg"+i);
            temp.setUserDTO(userDTO);

            boardList.add(temp);
        }

        result.put("boardList", boardList);

        result.put("pageNumber", 1);
        result.put("totalPage", 1);

        List<Integer> pagination = PageUtil.getStartEndPage(1, 1);
        result.put("pagination", pagination);

        given(boardService.list(boardSearchDto)).willReturn(result);

        mvc.perform(get("/board/list").params(mvm))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.boardList").isArray())
        .andExpect(jsonPath("$.pageNumber").value(1))
        .andExpect(jsonPath("$.totalPage").value(1))
        .andExpect(jsonPath("$.pagination").value(pagination))
        .andDo(print());
    }


    @Test
    @WithMockUser
    @DisplayName("게시글 등록")
    void postBoard() throws Exception{

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
        mvm.add("title", "제목");
        mvm.add("region", "서울");

        BoardDto boardDTO = new BoardDto();
        boardDTO.setTitle("제목");
        boardDTO.setRegion("서울");

        Map<String,Object> result = new HashMap<>();
        result.put("message", "success");

        given(boardService.save(boardDTO)).willReturn(result);

        mvc.perform(post("/board")
            .params(mvm))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("success"))
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 정보 가져오기")
    void board() throws Exception{

        Map<String, Object> result = new HashMap<>();
        BoardDto boardDto = new BoardDto();
        result.put("board", boardDto);
        result.put("message", "success");

        given(boardService.findByBoardId(1L)).willReturn(result);

        mvc.perform(get("/board/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.board").exists())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 수정")
    void updateBoard() throws Exception{

        BoardDto boardDto = new BoardDto();
        boardDto.setBoardId(1L);
        boardDto.setTitle("제목");
        boardDto.setRegion("서울");

        MultiValueMap<String , String> mvv = new LinkedMultiValueMap();
        mvv.set("boardId", "1");
        mvv.set("title", "제목");
        mvv.set("region" , "서울");

        Map<String ,Object> result = new HashMap<String, Object>();
        result.put("message", "success");

        given(boardService.updateBoard(boardDto)).willReturn(result);

        mvc.perform(put("/board/1").params(mvv))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("success"))
            .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 삭제")
    void deleteBoard() throws Exception{
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardIdArray("1,2,3");

        Map<String, Object> result = new HashMap<>();
        result.put("message", "success");

        given(boardService.deleteBoard(boardDto)).willReturn(result);

        mvc.perform(delete("/board").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(boardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("의견 등록")
    void postOpinion() throws Exception{
        BoardDto boardDto = new BoardDto();

        Map<String, Object> result = new HashMap<>();
        result.put("message", "success");

        given(boardService.postOpinion(boardDto)).willReturn(result);

        mvc.perform(post("/board/opinion").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(boardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("의견 삭제")
    void deleteOpinion() throws Exception{
        BoardDto boardDto = new BoardDto();

        Map<String, Object> result = new HashMap<>();
        result.put("message", "success");

        given(boardService.deleteOpinion(boardDto)).willReturn(result);

        mvc.perform(delete("/board/opinion").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(boardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("의견 조회")
    void getUserOpinion() throws Exception{
        BoardDto boardDto = new BoardDto();

        Map<String, Object> result = new HashMap<>();
        OpinionDto opinionDto = new OpinionDto();
        result.put("message", "success");
        result.put("opinion", opinionDto);

        given(boardService.getUserOpinion(boardDto)).willReturn(result);

        mvc.perform(post("/board/userOpinion").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(boardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }
}
