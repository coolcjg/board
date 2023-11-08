package com.cjg.traveling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.UserDTO;
import com.cjg.traveling.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	// 회원 등록
	public User insertUser(UserDTO userDTO) {
		
		User user = new User();
		user.setName(userDTO.getName());
		
		return userRepository.save(user);
	}
	

}
