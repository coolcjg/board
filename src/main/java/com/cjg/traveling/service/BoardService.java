package com.cjg.traveling.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.domain.Board;
import com.cjg.traveling.dto.BoardDTO;
import com.cjg.traveling.repository.BoardRepository;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	public List<Board> list(BoardDTO boardDTO){
		Pageable paging = PageRequest.of(boardDTO.getPageNum()-1, 10);
		return boardRepository.findAll(paging);
	}

}
