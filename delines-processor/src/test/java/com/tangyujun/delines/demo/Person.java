package com.tangyujun.delines.demo;

import com.tangyujun.delines.AbstractDelinesEntity;
import com.tangyujun.delines.annotation.DelinesEntity;
import com.tangyujun.delines.annotation.DelinesField;

import java.time.LocalDate;

@DelinesEntity(required = "P\\d+.*")
public class Person extends AbstractDelinesEntity {

	@DelinesField(regExp = "(?<=P)\\d+")
	private Integer id;

	@DelinesField(regExp = "[\\u4e00-\\u9fa5]+")
	private String name;

	@DelinesField(regExp = "\\b\\d{1,3}\\b")
	private Integer age;

	@DelinesField(regExp = "\\b[FM]\\b")
	private String sex;

	@DelinesField(regExp = "\\b[FM]\\b", decoder = IsManParser.class)
	private Boolean isMan;

	@DelinesField(regExp = "\\b[0-9]{8}", dateFormat = "yyyyMMdd", decodeExceptionHandler = ParseExceptionHandler.class)
	private LocalDate birthday;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getAge() {
		return age;
	}

	public String getSex() {
		return sex;
	}

	public Boolean getMan() {
		return isMan;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", sex='" + sex + '\'' +
				", isMan=" + isMan +
				", birthday=" + birthday +
				'}';
	}
}
