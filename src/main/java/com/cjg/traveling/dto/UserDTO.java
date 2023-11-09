package com.cjg.traveling.dto;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;

import com.cjg.traveling.domain.Address;

import lombok.Data;

@Data
public class UserDTO {
	
	@NotBlank(groups = UserDtoUpdate.class)
	private Long id;
	
	@NotBlank(message = "비밀번호를 입력해주세요.", groups = UserDtoInsert.class)
	private String password;
	
	@NotBlank(message = "이름을 입력해주세요.", groups = UserDtoInsert.class)
	private String name;
	
	private String phone;
	
	private String commnets;
	
	@Embedded
	private Address address;
	
	

}
