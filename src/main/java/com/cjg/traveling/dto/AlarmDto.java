package com.cjg.traveling.dto;

import lombok.Data;

@Data
public class AlarmDto {

	/*테스트*/
	private Long alarmId;
	private Long boardId;
	private String fromUserId;
	private String toUserId;
	private String regDate; 
	private String type;
	private String value;
	private String checked;

}
