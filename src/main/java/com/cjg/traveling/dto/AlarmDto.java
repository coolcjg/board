package com.cjg.traveling.dto;

import lombok.Data;

@Data
public class AlarmDto {
	
	private Long alarmId;
	private Long boardId;
	private String fromUserId;
	private String regDate; 
	private String type;
	private String value;

}
