package com.tangyujun.delines.validation.annotation;

import java.lang.annotation.*;

/**
 * 数值范围
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalRange {
	/**
	 * @return 最小值
	 */
	String min();

	/**
	 * @return 最小值
	 */
	String max();

	/**
	 * @return 提示信息
	 */
	String message() default "required min value!";
}