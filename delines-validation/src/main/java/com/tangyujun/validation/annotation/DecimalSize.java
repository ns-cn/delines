package com.tangyujun.validation.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalSize {
	/**
	 * 最小值
	 */
	String min();

	/**
	 * 最小值
	 */
	String max();

	/**
	 * 提示信息
	 */
	String message() default "required min value!";
}