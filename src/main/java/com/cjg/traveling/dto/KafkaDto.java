package com.cjg.traveling.dto;

import lombok.Data;

@Data
public class KafkaDto {
	
	private String type;
	private Long alarmId;
	private Long boardId;
	
	private String fromUserId;
	private String toUserId;
	
	private String value;
	private String regDate;
	private String message;
	
	private KafkaBoardDto kafkaBoardDto;

}
