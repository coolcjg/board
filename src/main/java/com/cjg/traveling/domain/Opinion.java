package com.cjg.traveling.domain;

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
}
