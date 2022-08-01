package cn.tangyujun.delines;

public class DelinesLine {

	private int lineIndex = 0;
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

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
