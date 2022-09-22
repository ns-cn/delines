package com.tangyujun.delines.validation.annotation;

import java.lang.annotation.*;

/**
 * 字符串长度限制
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringLength {
	/**
	 * @return 最小长度
	 */
	int min() default 0;

	/**
	 * @return 最大长度
	 */
	int max() default Integer.MAX_VALUE;

	/**
	 * @return 提示信息
	 */
	String message() default "required length!";
}