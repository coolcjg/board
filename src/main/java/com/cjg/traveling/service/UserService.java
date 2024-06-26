package com.cjg.traveling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
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
	
	@Setter
	@Value("${serverUrl}")
	private String serverUrl;	
	
	public Map<String,Object> existsById(String userId) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean exists = userRepository.existsByUserId(userId);
		
		map.put("message", "success");
		
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
			temp.setModDate(user.getModDate());
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
		result.put("message", "success");
		
		return result;
	}
	
	public Map<String,Object> user(String userId) {
		
		Map<String,Object> result = new HashMap<>();
		
		User user = findByUserId(userId);
		
		UserDto userDto = new UserDto();
		userDto.setUserId(user.getUserId());
		userDto.setAddress(user.getAddress());
		userDto.setAuth(user.getAuth());
		userDto.setBirthDay(user.getBirthDay());
		userDto.setName(user.getName());
		userDto.setPhone(user.getPhone());
		
		result.put("data", userDto);
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

		map.put("message", "success");

		return map; 
	}
	
	// 로그인
	public Map<String, Object> login(UserDto userDTO) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = userRepository.findByUserId(userDTO.getUserId());
		
		if(user == null) {
			map.put("message", "user empty");
		}else {
			
			if(user.getPassword().equals(encrypt.getEncrypt(userDTO.getPassword(), user.getSalt()))) {
				
				String accessToken = jwt.createAccessToken(user);
				String refreshToken = jwt.createRefreshToken(user);
				
				user.setRefreshToken(refreshToken);
				userRepository.save(user);

				map.put("message", "success");
				map.put("accessToken", accessToken);
				map.put("refreshToken", refreshToken);
				map.put("id", user.getUserId());
				map.put("name", user.getName());
				map.put("auth", user.getAuth());
			}else {
				map.put("message", "password not match");
			}
		}
		
		return map;
	}
	
	public Map<String, Object> putUser(UserDto userDto){
		Map<String, Object> result = new HashMap<String, Object>();
		
		User user = userRepository.findByUserId(userDto.getUserId());
		user.setPassword(encrypt.getEncrypt(userDto.getPassword(), user.getSalt()));
		user.setAuth(userDto.getAuth());
		user.setName(userDto.getName());
		user.setBirthDay(userDto.getBirthDay());
		
		result.put("message", "success");
		
		return result;
		
	}
	
	public Map<String, Object> delete(UserDto userDto){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		for(String userId : userDto.getUserIds()) {
			userRepository.deleteByUserId(userId);
		}
		
		result.put("message", "success");
		
		return result;
		
	}
	
}
