package com.cjg.traveling.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.domain.Opinion;
import com.cjg.traveling.repository.OpinionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OpinionService {
	
	private final OpinionRepository opinionRepository;
	
	public List<Opinion> findByBoard_boardId(Long boardId){
		return opinionRepository.findByBoard_boardId(boardId);
	}
	
	public Long deleteByBoard_boardId(Long boardId) {
		return opinionRepository.deleteByBoard_boardId(boardId);
	}

}
