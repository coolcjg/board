package com.cjg.traveling.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cjg.traveling.common.CustomAuthenticationEntryPoint;
import com.cjg.traveling.common.JwtTokenFilter;
import com.cjg.traveling.service.UserService;




@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtTokenFilter jwtTokenFilter;
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(apiConfigurationSource()))
			.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
					(authorizeRequests) -> authorizeRequests
						.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/upload/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/image/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/board", "/board/list", "/board/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/user/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/user", "/user/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/jwt/**").permitAll()						
					
						.requestMatchers(HttpMethod.POST, "/board").authenticated()
						.anyRequest().authenticated()													
			)
			.exceptionHandling((exception) -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();		
	}
	
	
	CorsConfigurationSource apiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
