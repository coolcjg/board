package com.cjg.traveling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.common.DateFormat;
import com.cjg.traveling.domain.Alarm;
import com.cjg.traveling.dto.AlarmDto;
import com.cjg.traveling.dto.AlarmSpecs;
import com.cjg.traveling.repository.AlarmRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {
	
	private final AlarmRepository alarmRepository;
	
	public Map<String, Object> list(Map<String, Object> map){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		int pageNumber=1;
		
		if(map.get("pageNumber") != null && !map.get("pageNumber").toString().equals("")) {
			pageNumber = Integer.parseInt(map.get("pageNumber").toString());
		}
		
		Specification<Alarm> specification = AlarmSpecs.searchWithUserId(map.get("userId").toString());
		PageRequest pageRequest = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "regDate");
		
		Page<Alarm> page = alarmRepository.findAll(specification, pageRequest);

		List<Alarm> alarmList  = page.getContent();
		List<AlarmDto> alarmDtolist = new ArrayList();
		
		for(Alarm alarm : alarmList) {
			AlarmDto alarmDto = new AlarmDto();
			alarmDto.setAlarmId(alarm.getAlarmId());
			alarmDto.setBoardId(alarm.getBoard().getBoardId());
			alarmDto.setFromUserId(alarm.getFromUser().getUserId());
			alarmDto.setRegDate(DateFormat.convertDateFormat(alarm.getRegDate()));
			alarmDto.setType(alarm.getType());
			alarmDto.setValue(alarm.getValue());
			alarmDtolist.add(alarmDto);
		}
		
		result.put("code", "200");
		result.put("list", alarmDtolist);
		
		return result;
		
	}
	
	

}
