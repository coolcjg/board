package com.cjg.traveling.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = {"user"})
public class Board {
	
	@Id @GeneratedValue
	@Column(name="BOARD_ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;
	
	// 제목
	private String title;
	
	// 내용
	private String contents;
	
	// 등록 날짜
	private Date regDate;
	
	// 수정일
	private Date modDate;
	
	public Board(User user) {
		setUser(user);
	}
	
	public void setUser(User user) {
		this.user = user;
		user.getBoardList().add(this);
	}
}
