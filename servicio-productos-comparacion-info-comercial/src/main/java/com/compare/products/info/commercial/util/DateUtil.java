package com.compare.products.info.commercial.util;

import java.time.LocalDateTime;

public class DateUtil {

	public static LocalDateTime textTodate(String date) {
		LocalDateTime dates = LocalDateTime.parse(date.substring(0, date.length()-6));
		return dates;
	}
	
}
