package com.cjg.traveling.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "mediaList")
public class Board {
	
	@Id
	@Column(name="BOARD_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardId;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
		
	// 제목
	private String title;
	
	// 내용
	private String contents;
	
	// 지역
	private String region;
	
	// 등록 날짜
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime regDate;
	
	// 수정일
	@LastModifiedDate
	private Date modDate;

	@OneToMany(mappedBy="board", orphanRemoval = true)
	private List<Media> mediaList;

	@OneToMany(mappedBy="board", orphanRemoval = true)
	@BatchSize(size=10)
	private List<Comment> commentList;
	
	// 조회수
	@Column(columnDefinition = "integer default 0")
	private int view;
}