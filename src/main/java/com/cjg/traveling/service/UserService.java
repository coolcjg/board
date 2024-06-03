package com.cjg.traveling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.common.Encrypt;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.PageUtil;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class UserService {
	
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Jwt jwt;
	
	@Autowired
	private Encrypt encrypt;
	
	@Value("${serverUrl}")
	private String serverUrl;	
	
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
	
	public Map<String, Object> list(UserDto userDto) {
		
		Map<String, Object> result = new HashMap<String,Object>();
		
		PageRequest pageRequest = PageRequest.of(userDto.getPageNumber()-1, userDto.getPageSize(), Sort.Direction.DESC, "regDate");
		Page<User> page =  userRepository.findAll(pageRequest);
		
		Map<String, Object> data = new HashMap<String, Object>();

		List<UserDto> userDtoList = new ArrayList();
		
		for(User user : page.getContent()) {
			UserDto temp = new UserDto();
			temp.setUserId(user.getUserId());
			temp.setName(user.getName());
			temp.setAuth(user.getAuth());
			temp.setRegDate(user.getRegDate());
			userDtoList.add(temp);
		}
		
		data.put("list", userDtoList);
		data.put("totalPages", page.getTotalPages());
		data.put("pageSize", page.getSize());
		data.put("pageNumber", page.getNumber()+1);
		
		data.put("prevList", page.getNumber() >=1 ? 
				(serverUrl + "/user/list?pageNumber=" + page.getNumber() + "&pageSize=" + page.getSize()) : "");
		
		data.put("nextList", page.getNumber() < page.getTotalPages()-1 ? 
				(serverUrl + "/user/list?pageNumber=" + (page.getNumber()+2) + "&pageSize=" + page.getSize()) : "");
		
		List<Integer> pagination = PageUtil.getStartEndPage(page.getNumber()+1, page.getTotalPages());
		data.put("pagination", pagination);
		
		result.put("data", data);
		result.put("code", 200);
		result.put("message", "success");
		
		return result;
	}
	
	public User findByUserId(String userId) {
		return userRepository.findByUserId(userId);
	}
	
	
	// 회원 등록
	public Map<String, Object> insertUser(UserDto userDTO) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = new User();
		String salt = encrypt.getSalt();
		
		user.setUserId(userDTO.getUserId());
		user.setSalt(salt);
		user.setPassword(encrypt.getEncrypt(userDTO.getPassword(), salt));
		user.setName(userDTO.getName());
		user.setBirthDay(userDTO.getBirthDay());
		user.setAuth(userDTO.getAuth());
		
		User resultUser = userRepository.save(user);
		
		if(resultUser.getUserId() == user.getUserId()) {
			map.put("code", "200");
		}else {
			map.put("code", "E-USER-001");
		}
		
		return map; 
	}
	
	// 로그인
	public Map<String, Object> login(UserDto userDTO, HttpServletResponse response) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = userRepository.findByUserId(userDTO.getUserId());
		
		if(user == null) {
			map.put("code", "E-USER-002");
		}else {
			
			if(user.getPassword().equals(encrypt.getEncrypt(userDTO.getPassword(), user.getSalt()))) {
				
				String accessToken = jwt.createAccessToken(user);
				String refreshToken = jwt.createRefreshToken(user);
				
				user.setRefreshToken(refreshToken);
				userRepository.save(user);
								
				map.put("code", "200");
				map.put("accessToken", accessToken);
				map.put("refreshToken", refreshToken);
				map.put("id", user.getUserId());
				map.put("name", user.getName());
			}else {
				map.put("code", "E-USER-003");
			}
		}
		
		return map;
	}
	
}
