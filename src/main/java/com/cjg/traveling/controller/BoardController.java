package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.dto.BoardDTO;
import com.cjg.traveling.service.BoardService;

@RestController
public class BoardController {
	
	@Autowired
	BoardService boardService;	
	
	@GetMapping("/board/list")
	public Map<String, Object> list(@RequestParam(required=false, defaultValue = "1") int pageNumber){
		
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setPageNumber(pageNumber);
		
		return boardService.list(boardDTO);	
	}

}
