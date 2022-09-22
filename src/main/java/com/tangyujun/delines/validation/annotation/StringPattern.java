package com.tangyujun.delines.validation.annotation;

import java.lang.annotation.*;

/**
 * 字符串正则限制
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringPattern {
	/**
	 * @return 值
	 */
	String value();

	/**
	 * @return 提示信息
	 */
	String message() default "required pattern!";
}
