package cn.tangyujun.delines.demo;

import cn.tangyujun.delines.AbstractDelinesEntity;
import cn.tangyujun.delines.annotation.DelinesEntity;
import cn.tangyujun.delines.annotation.DelinesField;

@DelinesEntity(rangeStartType = DelinesEntity.RangeType.NUMBER, rangeStart = "1", required = "[\\u4e00-\\u9fa5]+.*")
public class Score extends AbstractDelinesEntity {
	@DelinesField(regExp = "[\\u4e00-\\u9fa5]+")
	private String course;
	@DelinesField(regExp = "\\b\\d{1,3}\\b")
	private String score;

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Score{" +
				"course='" + course + '\'' +
				", score='" + score + '\'' +
				'}';
	}
}