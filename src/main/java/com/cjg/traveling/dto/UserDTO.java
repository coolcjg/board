package com.cjg.traveling.dto;

import javax.persistence.Embedded;

import com.cjg.traveling.domain.Address;

import lombok.Data;

@Data
public class UserDTO {
	
	private Long id;
	private String password;
	private String name;
	private String phone;
	private String commnets;
	
	@Embedded
	private Address address;
	
	

}
