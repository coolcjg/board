package com.cjg.traveling.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "boardList")
public class User {
	
	@Id @GeneratedValue
	@Column(name ="USER_ID")
	private Long id;
	
	private String password;
	
	private String salt;
	
	private String name;
	
	private String phone;
	
	private String commnets;
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	private List<Board> boardList= new ArrayList<Board>();
	
	@Embedded
	private Address address;
}
