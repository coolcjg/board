package com.cjg.traveling.dto;

import java.time.LocalDate;

import com.cjg.traveling.domain.Address;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {
	
	@NotBlank(groups = {UserDtoUpdate.class, UserDtoLogin.class})
	private String userId;
	
	@NotBlank(message = "비밀번호를 입력해주세요.", groups = {UserDtoInsert.class, UserDtoLogin.class})
	private String password;
	
	@NotBlank(message = "이름을 입력해주세요.", groups = UserDtoInsert.class)
	private String name;
	
	private String phone;
	
	private String commnets;
	
	@Embedded
	private Address address;
	
	private LocalDate birthDay;

}
