package com.cjg.traveling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cjg.traveling.domain.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, JpaSpecificationExecutor<Alarm>, AlarmCustomRepository {

	int deleteByAlarmId(Long alarmId);
	
	Alarm findByAlarmId(Long alarmId);
	

}
