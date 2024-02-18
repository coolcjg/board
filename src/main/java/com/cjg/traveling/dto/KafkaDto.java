package com.cjg.traveling.dto;

import lombok.Data;

@Data
public class KafkaDto {
	
	private String type;
	private Long opinionId;
	private Long boardId;
	private String userId;
	private String opinion;
	private String date;
	
	private KafkaBoardDto kafkaBoardDto;

}
