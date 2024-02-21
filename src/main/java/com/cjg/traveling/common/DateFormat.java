package com.cjg.traveling.common;

import java.time.LocalDateTime;

public class DateFormat {

	public static String convertDateFormat(LocalDateTime localDateTime) {
		String result = "";
		
		if(LocalDateTime.now().getYear() != localDateTime.getYear()) {
			result += (localDateTime.getYear() + "년 ");
		}
		
		result += (localDateTime.getMonthValue() + "월 " + localDateTime.getDayOfMonth() + "일");
		return result;
	}
}
