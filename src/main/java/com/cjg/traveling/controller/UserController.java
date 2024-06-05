package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.dto.UserDtoInsert;
import com.cjg.traveling.dto.UserDtoLogin;
import com.cjg.traveling.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/user/count/")
	public Map<String,Object> existsById(@RequestParam(value="userId",  required=true) String userId) {
		return userService.existsById(userId);
	}
	
	@GetMapping("/user/list")
	public Map<String, Object> list(UserDto user) {
		return userService.list(user);
	}	
	
	@PostMapping("/user")
	public Map<String, Object> insertUser(@RequestBody @Validated(UserDtoInsert.class) UserDto user) {
		return userService.insertUser(user);
	}
	
	@PutMapping("/user")
	public Map<String, Object> putUser(@RequestBody UserDto user){
		return userService.putUser(user);
	}
	
	@GetMapping("/user/{userId}")
	public Map<String, Object> user(@RequestBody @PathVariable("userId") String userId) {
		return userService.user(userId);
	}	
	
	@PostMapping("/user/login")
	public Map<String, Object> login(HttpServletResponse response, @RequestBody @Validated(UserDtoLogin.class) UserDto user) {
		return userService.login(user, response);
	}
	
	@DeleteMapping("/user")
	public Map<String, Object> delete(HttpServletResponse response, @RequestBody UserDto user) {
		return userService.delete(user);
	}
	
}
