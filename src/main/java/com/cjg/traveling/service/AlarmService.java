package com.cjg.traveling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.common.DateFormat;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.domain.Alarm;
import com.cjg.traveling.dto.AlarmDto;
import com.cjg.traveling.dto.AlarmSpecs;
import com.cjg.traveling.repository.AlarmRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {
	
	private final AlarmRepository alarmRepository;
	private final Jwt jwt;
	Logger logger = LoggerFactory.getLogger(AlarmService.class);
	
	public Map<String, Object> list(HttpServletRequest request, Map<String, Object> map){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(!isValidUserId(request, map)) {
			result.put("code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("message", "authFail");			
			return result;
		}
		
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
			alarmDtolist.add(setAlarmDto(alarm));
		}
		
		result.put("code", HttpServletResponse.SC_OK);
		result.put("list", alarmDtolist);
		result.put("count", page.getTotalElements());
		
		return result;
		
	}
	
	// 전달용 AlarmDto 처리
	public AlarmDto setAlarmDto(Alarm alarm) {
		AlarmDto alarmDto = new AlarmDto();
		alarmDto.setAlarmId(alarm.getAlarmId());
		alarmDto.setBoardId(alarm.getBoard().getBoardId());
		alarmDto.setFromUserId(alarm.getFromUser().getUserId());
		alarmDto.setToUserId(alarm.getToUser().getUserId());
		alarmDto.setRegDate(DateFormat.convertDateFormat(alarm.getRegDate()));
		alarmDto.setType(alarm.getType());
		alarmDto.setValue(alarm.getValue());
		alarmDto.setChecked(alarm.getChecked());
		
		return alarmDto;
	}
	
	public Map<String, Object> delete(HttpServletRequest request,  long alarmId){
	
		Map<String, Object> result = new HashMap<String, Object>();
		
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);
		
		Alarm alarm = alarmRepository.findByAlarmId(alarmId);
		
		if(userId.equals(alarm.getToUser().getUserId())) {
			
			int count = alarmRepository.deleteByAlarmId(alarmId);
			
			if(count == 1) {
				result.put("code", HttpServletResponse.SC_OK);
				result.put("message", "deleteOk");
			}else {
				result.put("code", HttpServletResponse.SC_NOT_MODIFIED);
				result.put("message", "targetNotFound");
			}			
			
		}else {
			result.put("code", HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "authFail");
		}
		
		logger.info("delete result : {}", result);
		
		return result;
	}

	//  JWT에 저장된 userId와 FORM 파라미터의 userId가 같은지 체크
	public boolean isValidUserId(HttpServletRequest request, Map<String, Object> map) {
		
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);		

		if(!userId.equals(map.get("userId").toString())) {
			return false;
		}else {
			return true;
		}
	}
	
	public Map<String, Object> check(HttpServletRequest request, Long alarmId){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);
		
		Alarm alarm = alarmRepository.findByAlarmId(alarmId);
		
		if(userId.equals(alarm.getToUser().getUserId())) {
			alarm.setChecked("Y");
			result.put("code", HttpServletResponse.SC_OK);
			result.put("message", "checked");
		}else {
			result.put("code", HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "authFail");			
		}
		
		logger.info("check result {}", result);
			
		return result;
		
	}


}
