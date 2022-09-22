package com.tangyujun.delines.annotation;

import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.decoder.SimpleDecoder;
import com.tangyujun.delines.handler.NoneHandler;

import java.lang.annotation.*;

/**
 * DelinesField: 解析的具体字段内容
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelinesField {

	/**
	 * @return 解析器
	 */
	Class<? extends IDelinesDecoder> decoder() default SimpleDecoder.class;

	/**
	 * @return 解析异常处理器，默认不做处理
	 */
	Class<? extends IDelinesDecoder.ExceptionHandler> decodeExceptionHandler() default NoneHandler.class;

	/**
	 * @return 正则表达式
	 */
	String value();

	/**
	 * @return 时间数据的默认格式化样式，用于默认的decoder处理时间类型
	 */
	String dateFormat() default "";
}
