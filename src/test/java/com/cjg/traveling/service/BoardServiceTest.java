package com.cjg.traveling.service;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import com.cjg.traveling.common.HttpRequestUtil;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.PageUtil;
import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.BoardDto;
import com.cjg.traveling.dto.BoardSpecs;
import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.repository.BoardRepository;
import com.cjg.traveling.repository.MediaRepository;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = { "uploadPath=20" })
public class BoardServiceTest {
	
	@Mock
	private BoardRepository boardRepository;
	
	@Mock
	private MediaRepository mediaRepository;
	
	@InjectMocks
	private MediaService mediaService;
	
	@Mock
	private HttpRequestUtil httpRequestUtil;
	
	@Mock
	private Jwt jwt;
	
	@Value("${uploadPath}")
	private static String uploadPath;
	
	@Value("${serverUrl}")
	private static String serverUrl;
	
	@Value("${encodeReturnUrl}")
	private static String encodeReturnUrl;
	
	@BeforeAll
	public static void setUp() {
	    ReflectionTestUtils.setField(BoardServiceTest.class, "uploadPath", "D:/NAS/upload/");
	    ReflectionTestUtils.setField(BoardServiceTest.class, "serverUrl", "http://localhost:8080");
	    ReflectionTestUtils.setField(BoardServiceTest.class, "encodeReturnUrl", "http://localhost:8080/api/encodingResult");
	}	
	
	@Test
	public void list(){
				
		List<Map<String,String>> paramList = new ArrayList();
		
		Map<String,String> map1 = new HashMap();
		map1.put("pageNumber", null);
		map1.put("searchType", "all");
		map1.put("searchText", "test");		
		
		Map<String,String> map2 = new HashMap();
		map2.put("pageNumber", "1");
		map2.put("searchType", "title");
		map2.put("searchText", "testTitle");
		
		Map<String,String> map3 = new HashMap();
		map3.put("pageNumber", "1");
		map3.put("searchType", "region");
		map3.put("searchText", "seoul");
		
		Map<String,String> map4 = new HashMap();
		map4.put("pageNumber", "1");
		map4.put("searchType", "userId");
		map4.put("searchText", "----");
		
		Map<String,String> map5 = new HashMap();
		map5.put("pageNumber", "1");
		map5.put("searchType", "regDate");
		map5.put("searchText", "2024-01-23~2024-01-23");
		
		Map<String,String> map6 = new HashMap();
		map6.put("pageNumber", "1");
		map6.put("searchType", null);
		map6.put("searchText", null);		
		
		paramList.add(map1);
		paramList.add(map2);
		paramList.add(map3);
		paramList.add(map4);
		paramList.add(map5);
		paramList.add(map6);
		
		List<Board> boardListMock = new ArrayList();
		
		User user = new User();
		user.setUserId("testId");
		
		Board boardMock1 = new Board();
		boardMock1.setBoardId(1l);
		boardMock1.setUser(user);
		
		Board boardMock2 = new Board();
		boardMock2.setBoardId(2l);
		boardMock2.setUser(user);
		
		Board boardMock3 = new Board();
		boardMock3.setBoardId(3l);
		boardMock3.setUser(user);
		
		Board boardMock4 = new Board();
		boardMock4.setBoardId(4l);
		boardMock4.setUser(user);
		
		Board boardMock5 = new Board();
		boardMock5.setBoardId(5l);
		boardMock5.setUser(user);
		
		boardListMock.add(boardMock1);
		boardListMock.add(boardMock2);
		boardListMock.add(boardMock3);
		boardListMock.add(boardMock4);
		boardListMock.add(boardMock5);
		
		for(Map<String, String> map : paramList) {
			
			Map<String, Object> result = new HashMap<String, Object>();
			
			BoardDto boardDTO = new BoardDto();
			int pageNumber=1;
			
			if(map.get("pageNumber") != null && !map.get("pageNumber").toString().equals("")) {
				pageNumber = Integer.parseInt(map.get("pageNumber").toString());
			}
			
			boardDTO.setPageNumber(pageNumber);
			
			Specification<Board> specification = null;
			if(map.get("searchText") != null && !map.get("searchText").toString().equals("")) {
				Map<String, Object> whereParam = new HashMap<String, Object>();
				whereParam.put("searchType", map.get("searchType"));
				whereParam.put("searchText", map.get("searchText"));
				specification = BoardSpecs.searchWith(whereParam);
			}
					
			PageRequest pageRequest = PageRequest.of(boardDTO.getPageNumber()-1, 10, Sort.Direction.DESC, "regDate");
			Page<Board> page;

			
			Pageable pageableMock = PageRequest.of(0, 10);
			page = new PageImpl(boardListMock, pageableMock, 10l);
			
			if(specification == null) {				
				given(boardRepository.findAll(pageRequest)).willReturn(page);
				page = boardRepository.findAll(pageRequest);
			}else {
				given(boardRepository.findAll(specification, pageRequest)).willReturn(page);
				page = boardRepository.findAll(specification, pageRequest);
			}
			
			List<BoardDto> boardList = new ArrayList();
			
			for(Board board : page.getContent()) {
				BoardDto temp = new BoardDto();
				
				temp.setBoardId(board.getBoardId());
				temp.setContents(board.getContents());
				temp.setRegDate(board.getRegDate());
				temp.setRegion(board.getRegion());
				temp.setTitle(board.getTitle());
				temp.setView(board.getView());
				
				UserDto userDTO = new UserDto();
				userDTO.setName(board.getUser().getName());
				userDTO.setUserId(board.getUser().getUserId());
				temp.setUserDTO(userDTO);
				
				boardList.add(temp);
				
			}
			
			result.put("code", "200");
			result.put("boardList", boardList);
			
			int totalPage = page.getTotalPages() == 0 ? 1 : page.getTotalPages();
			result.put("pageNumber", page.getPageable().getPageNumber()+1);
			result.put("totalPage", totalPage);
			
			List<Integer> pagination = PageUtil.getStartEndPage(pageNumber, totalPage);
			result.put("pagination", pagination);				
			
			Assertions.assertThat(boardList.size()).isEqualTo(boardListMock.size());
		}


	}

}
