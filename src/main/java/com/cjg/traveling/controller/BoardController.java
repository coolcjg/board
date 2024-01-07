package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.dto.BoardDTO;
import com.cjg.traveling.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class BoardController {
	
	@Autowired
	BoardService boardService;	
	
	@GetMapping("/board/list")
	public Map<String, Object> list(@RequestParam(required = false) Map<String, Object> map){
		return boardService.list(map);
	}
	
	@PostMapping(value ="/board")
	public Map<String, Object> board(HttpServletRequest request, BoardDTO boardDTO) throws Exception{
		return boardService.save(request, boardDTO);
	}		
	
	@GetMapping(value ="/board/{boardId}")
	public Map<String, Object> board(HttpServletRequest request, @PathVariable("boardId") long boardId) throws Exception{
		return boardService.findByBoardId(boardId);
	}	

}
