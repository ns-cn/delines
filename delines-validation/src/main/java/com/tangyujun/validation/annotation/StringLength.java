package com.tangyujun.validation.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringLength {
	/**
	 * 最小长度
	 */
	int min() default 0;

	/**
	 * 最大长度
	 */
	int max() default Integer.MAX_VALUE;

	/**
	 * 提示信息
	 */
	String message() default "required length!";
}