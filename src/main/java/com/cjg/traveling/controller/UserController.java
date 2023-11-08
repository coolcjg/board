package com.cjg.traveling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.UserDTO;
import com.cjg.traveling.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/user")
	public User insertUser(@RequestBody UserDTO user) {
		return userService.insertUser(user);
	}
	
}
