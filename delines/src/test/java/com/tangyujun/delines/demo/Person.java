package com.tangyujun.delines.demo;

import com.tangyujun.delines.AbstractDelinesEntity;
import com.tangyujun.delines.annotation.DelinesEntity;
import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.annotation.DelinesNestedField;

import java.time.LocalDate;

@DelinesEntity(required = "P\\d+.*")
public class Person extends AbstractDelinesEntity {

	@DelinesField(value = "(?<=P)\\d+")
	private Integer id;

	@DelinesField(value = "[\\u4e00-\\u9fa5]+")
	private String name;

	@DelinesField(value = "\\b\\d{1,3}\\b")
	private Integer age;

	@DelinesField(value = "\\b[FM]\\b")
	private String sex;

	@DelinesField(value = "\\b[FM]\\b", decoder = IsManParser.class)
	private Boolean isMan;

	@DelinesField(value = "\\b[0-9]{8}", dateFormat = "yyyyMMdd", decodeExceptionHandler = ParseExceptionHandler.class)
	private LocalDate birthday;

	@DelinesNestedField
	private MyDate birth;

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
				", birth=" + birth +
				'}';
	}
}
