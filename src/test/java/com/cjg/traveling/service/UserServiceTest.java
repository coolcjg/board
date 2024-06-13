package com.cjg.traveling.service;

import com.cjg.traveling.common.Encrypt;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.TestPropertyUtil;
import com.cjg.traveling.domain.Address;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;
	
	@Mock
	private Jwt jwt;
	
	@InjectMocks
	private Encrypt encryptInject;

	@Mock
	private Encrypt encryptMock;

	@Test
	@DisplayName("아이디 존재 유무 확인 : 존재")
	public void existsById() {
		
		String userId = "coolcjg";

		given(userRepository.existsByUserId(userId)).willReturn(true);
		Map<String, Object> result = userService.existsById(userId);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
		Assertions.assertThat(result.get("count")).isEqualTo(1);

	}

	@Test
	@DisplayName("아이디 존재 유무 확인 : 존재없음")
	public void existsById_2() {

		String userId = "coolcjg";

		given(userRepository.existsByUserId(userId)).willReturn(false);
		Map<String, Object> result = userService.existsById(userId);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
		Assertions.assertThat(result.get("count")).isEqualTo(0);

	}

	@Test
	@DisplayName("유저 리스트")
	public void list(){

		userService.setServerUrl(TestPropertyUtil.serverUrl);

		UserDto userDto = new UserDto();
		userDto.setPageNumber(2);
		userDto.setPageSize(1);

		List<User> list = new ArrayList();
		User user = new User();
		user.setUserId("coolcjg1");
		user.setName("최종규");
		user.setAuth("admin");
		user.setRegDate(LocalDateTime.now());
		user.setModDate(LocalDateTime.now());
		list.add(user);

		Pageable pageRequest = PageRequest.of(userDto.getPageNumber()-1, userDto.getPageSize(), Sort.Direction.DESC, "regDate");
		Page<User> page = new PageImpl<>(list, pageRequest,3);

		given(userRepository.findAll(pageRequest)).willReturn(page);

		Map<String, Object> result = userService.list(userDto);

		Assertions.assertThat(result.get("data")).isNotNull();
		Assertions.assertThat(result.get("message")).isEqualTo("success");
	}

	@Test
	@DisplayName("유저 정보 가져오기")
	public void user(){
		String userId = "coolcjg";

		User user = new User();
		user.setUserId("coolcjg");
		user.setAddress(new Address());
		user.setAuth("admin");
		user.setBirthDay(LocalDate.now());
		user.setName("최종규");
		user.setPhone("010-6582-7942");

		given(userRepository.findByUserId(userId)).willReturn(user);

		Map<String,Object> result = userService.user(userId);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
		Assertions.assertThat(result.get("data")).isNotNull();

	}



	// 회원 등록
	@Test
	@DisplayName("사용자 등록")
	public void insertUser() {

		//given
		UserDto userDTO = new UserDto();
		userDTO.setUserId("testUser");
		userDTO.setPassword("1234");
		userDTO.setName("테스트");
		userDTO.setBirthDay(LocalDate.of(1990, 1, 27));

		User user = new User();
		String salt = encryptInject.getSalt();

		user.setUserId(userDTO.getUserId());
		user.setSalt(salt);
		user.setPassword(encryptInject.getEncrypt(userDTO.getPassword(), salt));
		user.setName(userDTO.getName());
		user.setBirthDay(userDTO.getBirthDay());

		String encodedPassword = encryptInject.getEncrypt(userDTO.getPassword(), salt);

		given(encryptMock.getSalt()).willReturn(salt);
		given(encryptMock.getEncrypt(userDTO.getPassword(), salt)).willReturn(encodedPassword);
		given(userRepository.save(user)).willReturn(user);

		//when
		Map<String, Object> result = userService.insertUser(userDTO);

		//then
		Assertions.assertThat(result.get("message")).isEqualTo("success");
		 
	}
	
	
	// 로그인
	@Test
	@DisplayName("로그인 : 정상 로직")
	public void login() {

		//given
		UserDto userDto = new UserDto();
		userDto.setUserId("testId");
		userDto.setPassword("1234");

		User user = new User();
		user.setUserId(userDto.getUserId());
		user.setPassword("1234");
		String salt = encryptInject.getSalt();
		user.setSalt(salt);


		given(userRepository.findByUserId(userDto.getUserId())).willReturn(user);
		given(encryptMock.getEncrypt(userDto.getPassword(), user.getSalt())).willReturn(user.getPassword());
		given(jwt.createAccessToken(any(User.class))).willReturn("abc");
		given(jwt.createRefreshToken(any(User.class))).willReturn("bca");

		Map<String, Object> result = userService.login(userDto);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
	}

	@Test
	@DisplayName("로그인 : 비번이 맞지 않을 때")
	public void login_2() {

		//given
		UserDto userDto = new UserDto();
		userDto.setUserId("testId");
		userDto.setPassword("1234");

		User user = new User();
		user.setUserId(userDto.getUserId());
		user.setPassword("1234");
		String salt = encryptInject.getSalt();
		user.setSalt(salt);

		given(userRepository.findByUserId(userDto.getUserId())).willReturn(user);
		given(encryptMock.getEncrypt(userDto.getPassword(), user.getSalt())).willReturn(user.getPassword() + "1");

		Map<String, Object> result = userService.login(userDto);

		Assertions.assertThat(result.get("message")).isEqualTo("password not match");
	}

	@Test
	@DisplayName("로그인 : 아이디 없을 때")
	public void login_3() {

		//given
		UserDto userDto = new UserDto();
		userDto.setUserId("testId");
		userDto.setPassword("1234");

		given(userRepository.findByUserId(userDto.getUserId())).willReturn(null);

		Map<String, Object> result = userService.login(userDto);

		Assertions.assertThat(result.get("message")).isEqualTo("user empty");
	}

	@Test
	@DisplayName("사용자 수정")
	public void putUser(){

		UserDto userDto = new UserDto();
		userDto.setUserId("testId");
		userDto.setPassword("1234");
		userDto.setAuth("admin");
		userDto.setName("홍길동");
		userDto.setBirthDay(LocalDate.now());

		User user = new User();
		user.setUserId(userDto.getUserId());
		user.setSalt("testSalt");

		given(userRepository.findByUserId(userDto.getUserId())).willReturn(user);
		given(encryptMock.getEncrypt(userDto.getPassword(), user.getSalt())).willReturn("testPassword");

		Map<String,Object> result = userService.putUser(userDto);

		Assertions.assertThat(result.get("message")).isEqualTo("success");

	}

	@Test
	@DisplayName("사용자 삭제")
	public void delete(){

		UserDto userDto = new UserDto();
		userDto.setUserIds(new String[]{"test"});

		Map<String, Object> result = userService.delete(userDto);

		Assertions.assertThat(result.get("message")).isEqualTo("success");

	}

}
