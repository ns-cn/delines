package cn.tangyujun.delines.decoder;

import cn.tangyujun.delines.DelinesBusField;

import java.lang.reflect.Field;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

/**
 * 反序列化实现方式
 *
 * @param <T> 目标类型
 */
public interface IDelinesDecoder{
	/**
	 * 反序列化执行内容
	 *
	 * @param result 文本内容匹配结果
	 * @param field 字段信息
	 * @return 解析的结果
	 */
	<T> T decode(Matcher result, DelinesBusField field);

	@FunctionalInterface
	interface ExceptionHandler {
		/**
		 * 异常处理
		 * @param data 原始数据
		 * @param entity 实体
		 * @param field 实体的对应字段
		 * @param exception 反序列化发生的异常
		 */
		void handle(Matcher matcher, Object entity, Field field, Throwable exception);
	}
}
