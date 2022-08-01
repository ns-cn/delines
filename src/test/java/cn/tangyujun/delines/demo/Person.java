package cn.tangyujun.delines.demo;

import cn.tangyujun.delines.IDelinesEntity;
import cn.tangyujun.delines.annotation.DelinesField;

public class Person implements IDelinesEntity {
	
	@DelinesField(regExp = "(?<=P)\\d+")
	private Integer id;

	@DelinesField(regExp = "[\\u4e00-\\u9fa5]+")
	private String name;

	@DelinesField(regExp = "(?<=\\s)[FM]$")
	private String sex;

	@DelinesField(regExp = "(?<=\\s)[FM]$", decoder = IsManParser.class)
	private Boolean isMan;
	
	@Override
	public int getLineIndex() {
		return 0;
	}

	@Override
	public void setLineIndex(int lineIndex) {
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", sex='" + sex + '\'' +
				", isMan=" + isMan +
				'}';
	}
}
