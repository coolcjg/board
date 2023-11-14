package com.cjg.traveling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.domain.Board;
import com.cjg.traveling.dto.BoardDTO;
import com.cjg.traveling.enums.ExceptionEnum;
import com.cjg.traveling.exception.ApiException;
import com.cjg.traveling.service.BoardService;

@RestController
public class BoardController {
	
	@Autowired
	BoardService boardService;	
	
	@GetMapping("/board/list")
	public List<Board> list(@RequestParam(required=false, defaultValue = "1") int pageNum){
		
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setPageNum(pageNum);
		
		return boardService.list(boardDTO);	
	}

}
