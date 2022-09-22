package com.tangyujun.delines.validation.annotation;

import java.lang.annotation.*;

/**
 * 数值最大限制
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalMax {
	/**
	 * @return 值
	 */
	String value();

	/**
	 * @return 提示信息
	 */
	String message() default "required max value!";
}
