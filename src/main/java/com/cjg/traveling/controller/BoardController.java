package com.cjg.traveling.controller;

import java.util.Map;

import com.cjg.traveling.common.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.dto.BoardDto;
import com.cjg.traveling.dto.BoardDtoInsert;
import com.cjg.traveling.dto.BoardDtoUpdate;
import com.cjg.traveling.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class BoardController {

	@Autowired
	private Jwt jwt;
	
	@Autowired
	BoardService boardService;	
	
	@GetMapping("/board/list")
	public Map<String, Object> list(@RequestParam(required = false) Map<String, String> map){
		return boardService.list(map);
	}
	
	@PostMapping(value ="/board")
	public Map<String, Object> board(HttpServletRequest request, @Validated(BoardDtoInsert.class) BoardDto boardDTO) throws Exception{
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);
		boardDTO.setUserId(userId);
		return boardService.save(boardDTO);
	}
		
	@GetMapping(value ="/board/{boardId}")
	public Map<String, Object> board(HttpServletRequest request, @PathVariable("boardId") long boardId) throws Exception{
		return boardService.findByBoardId(boardId);
	}
	
	@PutMapping(value ="/board/{boardId}")
	public Map<String, Object> updateBoard(HttpServletRequest request, @Validated(BoardDtoUpdate.class) BoardDto boardDto) throws Exception{
		return boardService.updateBoard(boardDto);
	}	
	
	@DeleteMapping(value ="/board")
	public Map<String, Object> deleteBoard(HttpServletRequest request, @RequestBody BoardDto boardDTO) throws Exception{
		return boardService.deleteBoard(boardDTO);
	}
	
	@PostMapping(value ="/board/opinion")
	public Map<String, Object> postOpinion(@RequestBody BoardDto boardDTO) throws Exception{
		return boardService.postOpinion(boardDTO);
	}
	
	@DeleteMapping(value ="/board/opinion")
	public Map<String, Object> deleteOpinion(@RequestBody BoardDto boardDTO) throws Exception{
		return boardService.deleteOpinion(boardDTO);
	}
	
	@PostMapping(value ="/board/userOpinion")
	public Map<String, Object> getUserOpinion(@RequestBody BoardDto boardDTO) throws Exception{
		return boardService.getUserOpinion(boardDTO);
	}
	
}
