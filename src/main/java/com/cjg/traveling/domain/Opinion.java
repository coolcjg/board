package com.cjg.traveling.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Opinion {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long opinionId;
	
	@ManyToOne
	@JoinColumn(name = "boardId")
	Board board;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	User user;
	
	@Column(nullable = false, length = 1)
	private String opinion;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")	
	private LocalDateTime regDate;
}
