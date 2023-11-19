package com.cjg.traveling.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.common.Encrypt;
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
	public User insertUser(UserDTO userDTO) {
		
		User user = new User();
		String salt = encrypt.getSalt();
		
		user.setName(userDTO.getName());
		user.setSalt(salt);
		user.setPassword(encrypt.getEncrypt(userDTO.getPassword(), salt));
		
		return userRepository.save(user);
	}
	
	
	

}
