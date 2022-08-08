package com.tangyujun.delines.boot;

import com.tangyujun.delines.DelinesDocument;

import java.io.BufferedReader;

/**
 * 通过Spring的Bean工厂解析
 */
public class SpringDelinesDocument {

	/**
	 * 通过文本构建文档
	 *
	 * @param data 文本数据
	 * @return 文档
	 */
	public static DelinesDocument of(String data) {
		return DelinesDocument.of(data).withCustomEntityParser(SpringDelines.DELINES_ENTITY_SPRING_PARSER);
	}

	/**
	 * 通过流的方式构建文档
	 *
	 * @param reader 流
	 * @return 文档
	 */
	public static DelinesDocument of(BufferedReader reader) {
		return DelinesDocument.of(reader).withCustomEntityParser(SpringDelines.DELINES_ENTITY_SPRING_PARSER);
	}
}
