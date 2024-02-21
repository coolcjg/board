package com.cjg.traveling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cjg.traveling.domain.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, JpaSpecificationExecutor<Alarm> {
	
	Alarm findByBoard_boardIdAndFromUser_userId(Long boardId, String userId);
	
	Long deleteByBoard_boardIdAndFromUser_userId(Long boardId, String userId);
	
	

}
