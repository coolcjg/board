package com.cjg.traveling.service;

import com.cjg.traveling.domain.Opinion;
import com.cjg.traveling.repository.OpinionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OpinionService {

	@Autowired
	private OpinionRepository opinionRepository;
	
	public List<Opinion> findByBoard_boardId(Long boardId){
		return opinionRepository.findByBoard_boardId(boardId);
	}
	
	public Long deleteByBoard_boardId(Long boardId) {
		return opinionRepository.deleteByBoard_boardId(boardId);
	}

}
