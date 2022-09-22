package com.tangyujun.delines.validation.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalMax {
	/**
	 * 值
	 */
	String value();

	/**
	 * 提示信息
	 */
	String message() default "required max value!";
}