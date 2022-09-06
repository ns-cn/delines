package com.tangyujun.delines;

public abstract class AbstractDelinesEntity implements IDelinesEntityWithIndex {
	/**
	 * 行索引
	 */
	private long lineIndex;

	/**
	 * {@link IDelinesEntityWithIndex#getLineIndex()}
	 */
	public long getLineIndex() {
		return lineIndex;
	}

	/**
	 * {@link IDelinesEntityWithIndex#setLineIndex(long)}
	 * @param lineIndex 行索引
	 */
	public void setLineIndex(long lineIndex) {
		this.lineIndex = lineIndex;
	}
}
