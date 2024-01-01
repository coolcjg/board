package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.dto.MediaDTO;
import com.cjg.traveling.service.ApiService;

@RestController
public class ApiController {
	
	@Autowired
	ApiService apiService;
	
	@PostMapping("/api/encodingResult")
	public Map<String, Object> encodingResult(MediaDTO dto){
		return apiService.encodingResult(dto);	
	}
	
	

}
