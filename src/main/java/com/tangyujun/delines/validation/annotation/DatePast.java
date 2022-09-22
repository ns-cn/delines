package com.tangyujun.delines.validation.annotation;


import java.lang.annotation.*;

/**
 * 时间之前
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatePast {
	/**
	 * @return 值, 如果不指定则为当前时间，否则为指定的时间，时间格式yyyyMMdd或则yyyyMMddHHmmss
	 */
	String value() default "";

	/**
	 * @return 日期格式，可以使用其他的日期格式，例如指定时刻
	 */
	String format() default "yyyyMMdd";
	/**
	 * @return 是否包含声明时间点
	 */
	boolean contain() default true;
	/**
	 * @return 提示信息
	 */
	String message() default "required past time";
}
