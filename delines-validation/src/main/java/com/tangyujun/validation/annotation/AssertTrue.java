package com.tangyujun.validation.annotation;


import java.lang.annotation.*;

/**
 * 断言为true，必须注到布尔类型上
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertTrue {
	/**
	 * 提示信息
	 */
	String message() default "assert true";
}
