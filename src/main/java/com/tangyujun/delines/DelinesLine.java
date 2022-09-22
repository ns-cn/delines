package com.tangyujun.delines;

/**
 * 原字符串和行数信息的行字符串
 */
public class DelinesLine {

	/**
	 * 默认行为0
	 */
	private long lineIndex = 0;
	/**
	 * 字符串数据
	 */
	private String data;

	/**
	 * DelinesLine
	 */
	public DelinesLine() {

	}

	/**
	 * DelinesLine
	 *
	 * @param data 字符串数据
	 */
	public DelinesLine(String data) {
		this.data = data;
	}

	/**
	 * DelinesLine
	 *
	 * @param lineIndex 行数
	 * @param data      字符串数据
	 */
	public DelinesLine(int lineIndex, String data) {
		this.lineIndex = lineIndex;
		this.data = data;
	}

	/**
	 * 字符串数据
	 *
	 * @param data 字符串数据
	 * @return DelinesLine
	 */
	public static DelinesLine of(String data) {
		return new DelinesLine(data);
	}

	/**
	 * 构建DelinesLine
	 *
	 * @param lineIndex 行数
	 * @param data      字符串数据
	 * @return DelinesLine
	 */
	public static DelinesLine of(int lineIndex, String data) {
		return new DelinesLine(lineIndex, data);
	}

	/**
	 * 获取数据的行数
	 *
	 * @return 行数
	 */
	public long getLineIndex() {
		return lineIndex;
	}

	/**
	 * 设置行数
	 *
	 * @param lineIndex 行数
	 */
	public void setLineIndex(long lineIndex) {
		this.lineIndex = lineIndex;
	}

	/**
	 * 获取字符串数据
	 *
	 * @return 字符串数据
	 */
	public String getData() {
		return data;
	}

	/**
	 * 设置字符串数据
	 *
	 * @param data 字符串数据
	 */
	public void setData(String data) {
		this.data = data;
	}
}
