package com.tangyujun.delines;

/**
 * 抽象的带行的解析实体
 */
public abstract class AbstractDelinesEntity implements IDelinesEntity {
	/**
	 * 行索引
	 */
	private long lineIndex;

	/**
	 * {@link IDelinesEntity#getLineIndex()}
	 */
	public long getLineIndex() {
		return lineIndex;
	}

	/**
	 * {@link IDelinesEntity#setLineIndex(long)}
	 * @param lineIndex 行索引
	 */
	public void setLineIndex(long lineIndex) {
		this.lineIndex = lineIndex;
	}
}
