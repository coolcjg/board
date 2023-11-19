package com.cjg.traveling.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data

public class User {
	
	@Id
	@Column(name ="USER_ID")
	private String userId;
	
	private String password;
	
	private String salt;
	
	private String name;
	
	private String phone;
	
	private String commnets;
	
	@Embedded
	private Address address;
}
