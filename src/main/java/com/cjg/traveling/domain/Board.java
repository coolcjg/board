package com.cjg.traveling.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public class Board {
	
	@Id @GeneratedValue
	@Column(name="BOARD_ID")
	private Long id;
		
	// 제목
	private String title;
	
	// 내용
	private String contents;
	
	// 등록 날짜
	private Date regDate;
	
	// 수정일
	private Date modDate;
	
	
}
