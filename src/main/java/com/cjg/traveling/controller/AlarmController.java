package com.cjg.traveling.controller;

import java.util.Map;

import com.cjg.traveling.common.Jwt;
import org.springframework.web.bind.annotation.*;

import com.cjg.traveling.service.AlarmService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AlarmController {
	
	private final AlarmService alarmService;
	private final Jwt jwt;
	
	@GetMapping("/alarm/list")
	public Map<String, Object> list(HttpServletRequest request, @RequestParam(required = false) Map<String, Object> map){
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);
		map.put("accessTokenUserId", userId);
		return alarmService.list(map);
	}
	
	@DeleteMapping("/alarm")
	public Map<String, Object> deleteAlarm(HttpServletRequest request, @RequestParam long alarmId){
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);

		return alarmService.delete(alarmId, userId);
	}
	
	@PutMapping("/alarm/check/{alarmId}")
	public Map<String, Object> check(HttpServletRequest request, @PathVariable("alarmId") long alarmId){
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);
		return alarmService.check(userId, alarmId);
	}	

}
