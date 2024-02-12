package com.cjg.traveling.dto;

import lombok.Data;

@Data
public class OpinionDto {
	
	private Long opinionId;
	private Long boardId;
	private String userId;
	private String opinion;

}
