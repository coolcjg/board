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

import com.cjg.traveling.domain.Board;
import com.cjg.traveling.dto.BoardDTO;
import com.cjg.traveling.exception.ApiException;
import com.cjg.traveling.exception.ExceptionEnum;
import com.cjg.traveling.repository.BoardRepository;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	public Map<String, Object> list(BoardDTO boardDTO){
		
		Map<String, Object> map = new HashMap();
				
		PageRequest pageRequest = PageRequest.of(boardDTO.getPageNumber()-1, 10);
		Page<Board> page = boardRepository.findPageBy(pageRequest);
		
		map.put("code", "200");
		map.put("boardList", page.getContent());
		map.put("pageSize", page.getTotalPages());
		map.put("pageNumber", page.getPageable().getPageNumber()+1);
		
		return map;
	}

}
