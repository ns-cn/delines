package com.tangyujun.delines.annotation;

import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.decoder.SimpleDecoder;
import com.tangyujun.delines.exceptionhandler.NoneHandler;
import com.tangyujun.delines.validator.DelinesValidator;

import java.lang.annotation.*;

/**
 * DelinesField: 解析的具体字段内容
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelinesField {

	/**
	 * 解析器
	 */
	Class<? extends IDelinesDecoder> decoder() default SimpleDecoder.class;

	/**
	 * 解析异常处理器，默认不做处理
	 */
	Class<? extends IDelinesDecoder.ExceptionHandler> decodeExceptionHandler() default NoneHandler.class;

	/**
	 * 正则表达式
	 */
	String regExp();

	/**
	 * 时间数据的默认格式化样式，用于默认的decoder处理时间类型
	 */
	String dateFormat() default "";

	/**
	 * 字段级别的校验
	 */
	Class<? extends DelinesValidator> validator() default DelinesValidator.None.class;
}
