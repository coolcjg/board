package com.cjg.traveling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cjg.traveling.domain.Opinion;

public interface OpinionRepository extends JpaRepository<Opinion, Long>, JpaSpecificationExecutor<Opinion>, OpinionCustomRepository{
	
//	Opinion findByBoard_boardIdAndUser_userId(Long boardId, String userId);
//
//	Long deleteByBoard_boardIdAndUser_userId(Long boardId, String userId);
//
//	List<Opinion> findByBoard_boardId(Long boardId);
//
//	Long deleteByBoard_boardId(Long boardId);

}
