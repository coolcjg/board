package com.cjg.traveling.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Address {
	
	//도시
	private String city;
	
	//도로명
	private String roadName;
	
	// 우편 번호
	private String zipCode;

}
