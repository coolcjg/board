package com.cjg.traveling.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cjg.traveling.dto.BoardDTO;
import com.cjg.traveling.service.BoardService;

@RestController
public class BoardController {
	
	@Autowired
	BoardService boardService;	
	
	@GetMapping("/board/list")
	public Map<String, Object> list(@RequestParam(name="pageNumber", required=false, defaultValue = "1") int pageNumber){
		
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setPageNumber(pageNumber);
		
		return boardService.list(boardDTO);	
	}
	
	@PostMapping(value ="/board")
	public String board(@RequestParam(value = "files", required=false) List<MultipartFile> files) throws Exception{
		
		for(MultipartFile uploadFile : files) {
			System.out.println("파일이름 : " + uploadFile.getOriginalFilename());
		}
		
		return "aaaa";
			
	}	

}
