package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.UserDTO;
import com.cjg.traveling.dto.UserDtoInsert;
import com.cjg.traveling.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/user/count/")
	public Map<String,Object> existsById(@RequestParam(required=true) String userId) {
		return userService.existsById(userId);
	}
	
	@PostMapping("/user")
	public User insertUser(@Validated(UserDtoInsert.class) UserDTO user) {
		return userService.insertUser(user);
	}
	
}
