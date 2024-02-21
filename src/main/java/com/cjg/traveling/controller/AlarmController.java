package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.service.AlarmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AlarmController {
	
	private final AlarmService alarmService;
	
	@GetMapping("/alarm/list")
	public Map<String, Object> list(@RequestParam(required = false) Map<String, Object> map){
		return alarmService.list(map);
	}

}
