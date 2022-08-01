package cn.tangyujun.delines.annotation;

import cn.tangyujun.delines.decoder.IDelinesDecoder;
import cn.tangyujun.delines.decoder.SimpleDecoder;
import cn.tangyujun.delines.exceptionhandler.NoneHandler;

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
}
