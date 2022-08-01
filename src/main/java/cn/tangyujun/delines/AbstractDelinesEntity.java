package cn.tangyujun.delines;

public abstract class AbstractDelinesEntity implements IDelinesEntity {
	/**
	 * 行索引
	 */
	private int lineIndex;

	/**
	 * {@link IDelinesEntity#getLineIndex()}
	 */
	public int getLineIndex() {
		return lineIndex;
	}

	/**
	 * {@link IDelinesEntity#setLineIndex(int)}
	 * @param lineIndex 行索引
	 */
	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}
}
