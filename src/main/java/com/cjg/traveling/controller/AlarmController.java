package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.service.AlarmService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AlarmController {
	
	private final AlarmService alarmService;
	
	@GetMapping("/alarm/list")
	public Map<String, Object> list(HttpServletRequest request, @RequestParam(required = false) Map<String, Object> map){
		return alarmService.list(request, map);
	}
	
	@DeleteMapping("/alarm/{alarmId}")
	public Map<String, Object> delete(HttpServletRequest request, @PathVariable("alarmId") long alarmId){
		return alarmService.delete(request, alarmId);
	}
	
	@PutMapping("/alarm/check/{alarmId}")
	public Map<String, Object> check(HttpServletRequest request, @PathVariable("alarmId") long alarmId){
		return alarmService.check(request, alarmId);
	}	

}
