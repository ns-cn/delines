package com.tangyujun.delines.annotation;

import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.decoder.SimpleDecoder;
import com.tangyujun.delines.exceptionhandler.NoneHandler;
import com.tangyujun.delines.validator.DelinesValidator;

import java.lang.annotation.*;

/**
 * DelinesItem: 行的子项
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelinesField {

	/**
	 * 显示名称，用于用户交互
	 */
	String name() default "";

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

	Class<? extends DelinesValidator> validator() default DelinesValidator.None.class;
}