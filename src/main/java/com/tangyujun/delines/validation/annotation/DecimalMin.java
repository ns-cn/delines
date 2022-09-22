package com.tangyujun.delines.validation.annotation;

import java.lang.annotation.*;

/**
 * 数值最小值
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalMin {
	/**
	 * @return 值
	 */
	String value();

	/**
	 * @return 提示信息
	 */
	String message() default "required min value!";
}
