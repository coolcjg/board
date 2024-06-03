package com.cjg.traveling.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@DynamicUpdate
public class User {
	
	@Id
	@Column(name ="USER_ID")
	private String userId;
	
	private String password;
	
	private String salt;
	
	private String name;
	
	private String phone;
	
	private String commnets;
	
	private LocalDate birthDay;
	
	@ColumnDefault("user")
	private String auth;
	
	@Embedded
	private Address address;
	
	private String refreshToken;
	
	// 등록 날짜
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime regDate;
	
	// 수정일
	@LastModifiedDate
	private Date modDate;
}
