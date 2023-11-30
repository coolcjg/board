package com.cjg.traveling.domain;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public class Board {
	
	@Id @GeneratedValue
	@Column(name="BOARD_ID")
	private Long boardId;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
		
	// 제목
	private String title;
	
	// 내용
	private String contents;
	
	// 등록 날짜
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-mm-dd hh:MM")
	private LocalDateTime regDate;
	
	// 수정일
	@LastModifiedDate
	private Date modDate;
	
	
	// 조회수
	@Column(columnDefinition = "integer default 0")
	private int view;
	
	
}
