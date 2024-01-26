package com.cjg.traveling.service;

import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cjg.traveling.common.Encrypt;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private Jwt jwt;
	
	@Mock
	private Encrypt encrypt;
	
	@Test
	public void existsById() {
		
		String userId = "testUser";
		
		boolean[] existsArray = {true, false};
		
		for(boolean existsTemp : existsArray) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			given(userRepository.existsByUserId(userId)).willReturn(existsTemp);
			boolean exists = userRepository.existsByUserId(userId);
			
			map.put("code", "200");
			
			if(exists == true) {
				map.put("count", 1);
			}else {
				map.put("count", 0);
			}
			
		}		
	}
	
	// 회원 등록
	@Test
	public void insertUser() {
		
		//given
		UserDto userDTO = new UserDto();
		userDTO.setUserId("testUser");
		userDTO.setPassword("1234");
		userDTO.setName("테스트");
		userDTO.setBirthDay(LocalDate.of(1990, 1, 27));
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = new User();
		String salt = encrypt.getSalt();
		System.out.println("salt : " + salt);
		
		user.setUserId(userDTO.getUserId());
		user.setSalt(salt);
		user.setPassword(encrypt.getEncrypt(userDTO.getPassword(), salt));
		user.setName(userDTO.getName());
		user.setBirthDay(userDTO.getBirthDay());
		
		//given
		given(userRepository.save(user)).willReturn(user);
		
		User resultUser = userRepository.save(user);
		
		if(resultUser.getUserId() == user.getUserId()) {
			map.put("code", "200");
		}else {
			map.put("code", "E-USER-001");
		}
		 
	}
	
	
	// 로그인
	@Test
	public void login() {
		
		//given
		UserDto userDTO1 = new UserDto();
		userDTO1.setUserId("testId");
		userDTO1.setPassword("1234");
		
		UserDto userDTO2 = new UserDto();
		userDTO2.setUserId("testId2");
		userDTO2.setPassword("1234");
		
		User user1 = new User();
		user1.setUserId("testId");
		user1.setPassword("1234");
		user1.setSalt("1234");
		
		UserDto[] userDtoArray = {userDTO1, userDTO2};
		User[] userArray = {user1, null};	
		
		for(int i=0; i<userDtoArray.length; i++) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			given(userRepository.findByUserId(userDtoArray[i].getUserId())).willReturn(userArray[i]);
			User user = userRepository.findByUserId(userDtoArray[i].getUserId());
			
			if(user == null) {
				map.put("code", "E-USER-002");
			}else {
				
				if(user.getPassword().equals(encrypt.getEncrypt(userDtoArray[i].getPassword(), user.getSalt()))) {
					
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
		}

	}	
}
