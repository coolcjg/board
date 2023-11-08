package com.cjg.traveling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	
	@GetMapping("/")
	public String main() {
		return "index.html";
	}
	
	@GetMapping("/page/user")
	public String userPage() {
		return "/User.html";
	}
	
}
