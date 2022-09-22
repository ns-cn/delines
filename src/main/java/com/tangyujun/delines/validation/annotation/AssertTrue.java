package com.tangyujun.delines.validation.annotation;


import java.lang.annotation.*;

/**
 * 断言为true，必须注到布尔类型上
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertTrue {
	/**
	 * @return 提示信息
	 */
	String message() default "assert true";
}
