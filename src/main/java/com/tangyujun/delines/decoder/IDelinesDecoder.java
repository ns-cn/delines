package com.tangyujun.delines.decoder;

import com.tangyujun.delines.DelinesBusField;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

/**
 * 反序列化实现方式
 */
public interface IDelinesDecoder {
	/**
	 * 反序列化执行内容
	 *
	 * @param result 文本内容匹配结果
	 * @param field  字段信息
	 * @return 解析的结果
	 */
	Object decode(Matcher result, DelinesBusField field);

	/**
	 * 解析异常处理器
	 */
	@FunctionalInterface
	interface ExceptionHandler {
		/**
		 * 异常处理
		 *
		 * @param matcher   匹配结果
		 * @param entity    实体
		 * @param field     实体的对应字段
		 * @param exception 反序列化发生的异常
		 */
		void handle(Matcher matcher, Object entity, Field field, Throwable exception);
	}
}
