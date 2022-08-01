package cn.tangyujun.delines;

import java.io.BufferedReader;
import java.io.StringReader;

public class DelinesDocument {
	/**
	 * 数据源,可包装各种字符流（网络IO，文件IO，或则内存的字符串）
	 */
	private BufferedReader reader;


	/**
	 * 根据{@link BufferedReader}构建待处理的
	 *
	 * @param reader {@link BufferedReader} 数据读取的来源
	 */
	public DelinesDocument(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 根据string内容构建待处理的Delines文档
	 *
	 * @param page 文本文件完整内容体
	 * @return {@link DelinesDocument}
	 */
	public static DelinesDocument of(String page) {
		return of(new BufferedReader(new StringReader(page)));
	}

	/**
	 * 根据{@link BufferedReader}构建待处理的Delines文档
	 *
	 * @param reader {@link BufferedReader}
	 * @return {@link DelinesDocument}
	 */
	public static DelinesDocument of(BufferedReader reader) {
		return new DelinesDocument(reader);
	}
}
