package com.cjg.traveling.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.BoardDTO;
import com.cjg.traveling.exception.ApiException;
import com.cjg.traveling.exception.ExceptionEnum;
import com.cjg.traveling.repository.BoardRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private Jwt jwt;
	
	public Map<String, Object> list(BoardDTO boardDTO){
		
		Map<String, Object> map = new HashMap();
				
		PageRequest pageRequest = PageRequest.of(boardDTO.getPageNumber()-1, 10);
		Page<Board> page = boardRepository.findPageBy(pageRequest);
		
		map.put("code", "200");
		map.put("boardList", page.getContent());
		map.put("totalPage", page.getTotalPages() == 0 ? 1 : page.getTotalPages());
		map.put("pageNumber", page.getPageable().getPageNumber()+1);
		
		return map;
	}
	
	public Map<String, Object> save(HttpServletRequest request, BoardDTO boardDTO){
		
		Map<String, Object> map = new HashMap();
		
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);
		
		User user = new User();
		user.setUserId(userId);
		
		Board board = new Board();
		board.setUser(user);
		board.setTitle(boardDTO.getTitle());
		board.setRegion(boardDTO.getRegion());
		board.setContents(boardDTO.getContents());
		
		boardRepository.save(board);
		
		map.put("code", "200");		
		return map;	
	}
	
	

}
