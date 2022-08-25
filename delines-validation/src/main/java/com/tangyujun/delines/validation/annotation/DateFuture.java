package com.tangyujun.delines.validation.annotation;


import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFuture {
	/**
	 * 值, 如果不指定则为当前时间，否则为指定的时间
	 * {@link #format()}
	 */
	String value() default "";

	/**
	 * 日期格式，可以使用其他的日期格式，例如指定时刻
	 */
	String format() default "yyyyMMdd";

	/**
	 * 提示信息
	 */
	String message() default "required future time";
}
