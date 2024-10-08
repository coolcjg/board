package com.cjg.traveling.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${reactDomain}")
	String reactDoamin;


	/*
	UsernamePasswordAuthenticationFilter.class에서 기본적으로 username/password방식으로 인증처리가 된다.
	JWT관련 JwtTokenFilter를 사용할 예정이므로 addFilterBefore를 사용하여 JwtTokenFilter가 먼저 걸리게 한다.
	 */
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(apiConfigurationSource()))
			.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
					(authorizeRequests) -> authorizeRequests
						.requestMatchers(HttpMethod.GET, "/upload/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/image/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/jwt/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/media/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api-docs", "/api-docs/**", "/swagger-ui/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/**").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/board", "/board/list", "/board/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/board/opinion").authenticated()
						.requestMatchers(HttpMethod.POST, "/board", "/board/**").authenticated()
						.requestMatchers(HttpMethod.DELETE, "/board", "/board/**").authenticated()
						.requestMatchers(HttpMethod.PUT, "/board/**").authenticated()
						
						.requestMatchers(HttpMethod.GET, "/user/count/").permitAll()
						.requestMatchers(HttpMethod.POST, "/user", "/user/login").permitAll()
						
						.requestMatchers(HttpMethod.DELETE, "/media/**").authenticated()
						
						.anyRequest().authenticated()
			)
			.exceptionHandling((exception) -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
	CorsConfigurationSource apiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}

