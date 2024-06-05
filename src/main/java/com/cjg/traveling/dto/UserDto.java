package com.cjg.traveling.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cjg.traveling.domain.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외
public class UserDto {
	
	@NotBlank(groups = {UserDtoUpdate.class, UserDtoLogin.class})
	private String userId;
	
	@NotBlank(message = "비밀번호를 입력해주세요.", groups = {UserDtoInsert.class, UserDtoLogin.class})
	private String password;
	
	@NotBlank(message = "이름을 입력해주세요.", groups = UserDtoInsert.class)
	private String name;
	
	@NotBlank(message = "권한을 입력해주세요.", groups = UserDtoInsert.class)
	private String auth;
	
	private String phone;
	
	private String commnets;
	
	@Embedded
	private Address address;
	
	private LocalDate birthDay;
		
	private Integer pageNumber;
	

	private Integer pageSize;
	
	private String[] userIds;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime regDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime modDate;		

}
