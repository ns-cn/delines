package com.tangyujun.delines.spring;

import com.tangyujun.delines.Delines;
import com.tangyujun.delines.DelinesBusEntity;
import com.tangyujun.delines.DelinesLine;
import com.tangyujun.delines.IDelinesEntity;
import com.tangyujun.delines.parser.DelinesEntityParser;
import com.tangyujun.delines.parser.DelinesFieldParser;

/**
 * 基于spring的单实体解析
 */
public class SpringDelines {

	/**
	 * 默认的Spring实体解析器
	 */
	public static final DelinesEntityParser DELINES_ENTITY_SPRING_PARSER = DelinesEntityParser.custom()
			.withFieldParser(DelinesFieldParser
					.custom()
					.withDecoderFactory(new SpringDecoderFactory())
					.withDecodeExceptionHandlerFactory(new SpringDecodeExceptionHandlerFactory()));

	/**
	 * 解析字符串为实体
	 *
	 * @param data  字符串
	 * @param clazz 目标实体类型
	 * @param <T>   类型泛型
	 * @return 解析后的实体
	 */
	public static <T> T with(String data, Class<T> clazz) {
		return Delines.with(data, clazz, DELINES_ENTITY_SPRING_PARSER);
	}

	/**
	 * 解析字符串为实体
	 *
	 * @param data   字符串
	 * @param entity 目标实体类型
	 * @param <T>    泛型类型
	 * @return 解析后的实体
	 */
	public static <T> T with(String data, DelinesBusEntity<T> entity) {
		return Delines.with(data, entity);
	}

	/**
	 * 解析带行号的字符串数据 为目标实体
	 *
	 * @param line   字符串数据
	 * @param entity 目标实体类型
	 * @param <T>    泛型类型
	 * @return 解析后的实体
	 */
	public static <T extends IDelinesEntity> T with(DelinesLine line, DelinesBusEntity<T> entity) {
		return Delines.with(line, entity);
	}
}
