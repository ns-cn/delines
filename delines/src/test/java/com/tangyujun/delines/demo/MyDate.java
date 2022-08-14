package com.tangyujun.delines.demo;

import com.tangyujun.delines.annotation.DelinesField;

public class MyDate {

	@DelinesField(regExp = "\\b[0-9]{4}(?=年)")
	private Integer year;
	@DelinesField(regExp = "(?<=年)[0-9]{1,2}(?=月)")
	private Integer month;
	@DelinesField(regExp = "(?<=月)[0-9]{1,2}(?=日)")
	private Integer day;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return String.format("%d-%02d-%02d", year, month, day);
	}
}
