package cn.tangyujun.delines.demo;

import cn.tangyujun.delines.IDelinesEntity;
import cn.tangyujun.delines.annotation.DelinesField;

public class DynFlight implements IDelinesEntity {
	
	@DelinesField(regExp = "(?<=P)\\d+")
	private Integer index;
	@DelinesField(regExp = "(?<=\\s)\\w{2}(?=\\d+{3,})")
	private String airlines;
	@DelinesField(regExp = "(?<=\\w{2})\\d{3,}")
	private String flightNo;

	@DelinesField(regExp = "(?<=\\s)[ADad](?=\\s)",decoder = IsOffInParser.class)
	private Boolean isA;

	@DelinesField(regExp = "(?<=[ADad]\\s)\\w+")
	private String craftNo;
	
	@Override
	public int getLineIndex() {
		return 0;
	}

	@Override
	public void setLineIndex(int lineIndex) {
	}

	@Override
	public String toString() {
		return "DynFlight{" +
				"index=" + index +
				", airlines='" + airlines + '\'' +
				", flightNo='" + flightNo + '\'' +
				", isA=" + isA +
				", craftNo='" + craftNo + '\'' +
				'}';
	}
}
