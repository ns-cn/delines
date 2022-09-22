package com.tangyujun.delines.annotation;

import java.lang.annotation.*;

/**
 * 可嵌套的解析字段内容
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelinesNestedField {

	/**
	 * 正则表达式，通过指定正则表达式截取完整字符串的部分，子字段的正则读取字符串则使用截取的部分
	 */
	String value() default "";
}