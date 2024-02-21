package com.cjg.traveling.status;

public enum AlarmType {
	
	LIKE("좋아요"),
	REPLY("댓글");
	
	private String text;
	
	private AlarmType(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

}
