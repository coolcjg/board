package com.cjg.traveling.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.JwtTokenFilter;
import com.cjg.traveling.service.UserService;


@Configuration
public class SecurityConfig implements WebMvcConfigurer {
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtTokenFilter jwtTokenFilter;
	
	@Autowired
	Jwt jwt;
	
	
	
	
	
	
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
					(authorizeRequests) -> authorizeRequests.requestMatchers(HttpMethod.GET, "/board", "/board/**").permitAll()
															.requestMatchers(HttpMethod.POST, "/board").authenticated()
															
															.requestMatchers(HttpMethod.GET, "/user/**").permitAll()
															.requestMatchers(HttpMethod.POST, "/user", "/user/login").permitAll()
															.anyRequest().authenticated()
					
					/*
					(authorizeRequests) -> authorizeRequests.anyRequest().permitAll()
					*/					
			);
		
		http
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();		
	}
	

}
