package com.tangyujun.delines.demo;

import com.tangyujun.delines.AbstractDelinesEntity;
import com.tangyujun.delines.annotation.DelinesEntity;
import com.tangyujun.delines.annotation.DelinesField;

@DelinesEntity(rangeStartType = DelinesEntity.RangeType.NUMBER, rangeStart = "1", required = "[\\u4e00-\\u9fa5]+.*")
public class Score extends AbstractDelinesEntity {
	@DelinesField(value = "[\\u4e00-\\u9fa5]+")
	private String course;
	@DelinesField(value = "\\d{1,3}\\b")
	private Integer score;

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Score{" +
				"course='" + course + '\'' +
				", score=" + score +
				'}';
	}
}