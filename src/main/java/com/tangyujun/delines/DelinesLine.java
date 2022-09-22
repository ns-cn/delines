package com.tangyujun.delines;

public class DelinesLine {

	private long lineIndex = 0;
	private String data;

	public DelinesLine() {

	}

	public DelinesLine(String data) {
		this.data = data;
	}

	public DelinesLine(int lineIndex, String data) {
		this.lineIndex = lineIndex;
		this.data = data;
	}

	public static DelinesLine of(String data) {
		return new DelinesLine(data);
	}

	public static DelinesLine of(int lineIndex, String data) {
		return new DelinesLine(lineIndex, data);
	}

	public long getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(long lineIndex) {
		this.lineIndex = lineIndex;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
