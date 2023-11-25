package com.cjg.traveling.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.common.Encrypt;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.UserDTO;
import com.cjg.traveling.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Encrypt encrypt;
	
	public Map<String,Object> existsById(String userId) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean exists = userRepository.existsByUserId(userId);
		
		map.put("code", "200");
		
		if(exists == true) {
			map.put("count", 1);
		}else {
			map.put("count", 0);
		}
		
		return map;
	}
	
	
	
	// 회원 등록
	public Map<String, Object> insertUser(UserDTO userDTO) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = new User();
		String salt = encrypt.getSalt();
		
		user.setUserId(userDTO.getUserId());
		user.setSalt(salt);
		user.setPassword(encrypt.getEncrypt(userDTO.getPassword(), salt));
		user.setName(userDTO.getName());
		user.setBirthDay(userDTO.getBirthDay());
		
		User resultUser = userRepository.save(user);
		
		if(resultUser.getUserId() == user.getUserId()) {
			map.put("code", "200");
		}else {
			map.put("code", "E-USER-001");
		}
		
		return map; 
	}
	
	// 로그인
	public Map<String, Object> login(UserDTO userDTO) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = userRepository.findByUserId(userDTO.getUserId());
		
		if(user == null) {
			map.put("code", "E-USER-002");
		}else {
			
			if(user.getPassword().equals(encrypt.getEncrypt(userDTO.getPassword(), user.getSalt()))) {
				
				Map<String, String> param = new HashMap();
				param.put("id", user.getUserId());
				param.put("name", user.getName());
				
				String jwt = Jwt.createJwtToken(param);
				String jwtRefresh = Jwt.createJwtRefreshToken(param);
				
				user.setRefreshToken(jwtRefresh);
				userRepository.save(user);
				
				map.put("code", "200");
				map.put("jwt", jwt);
				map.put("jwtRefresh", jwtRefresh);
			}else {
				map.put("code", "E-USER-003");
			}
		}
		
		return map;
	}	
	
	
	

}
