package cn.tangyujun.delines;

/**
 * Delines解析后的实体
 */
public interface IDelinesEntity {

	/**
	 * 获取当前实体所在行
	 * @return 行索引
	 */
	int getLineIndex() ;

	/**
	 * 设置行索引
	 * @param lineIndex 行索引
	 */
	void setLineIndex(int lineIndex);
}
