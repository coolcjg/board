package com.cjg.traveling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cjg.traveling.domain.Opinion;

public interface OpinionRepository extends JpaRepository<Opinion, Long>, JpaSpecificationExecutor<Opinion> {
	
	Opinion findByBoard_boardIdAndUser_userId(Long boardId, String userId);

}
