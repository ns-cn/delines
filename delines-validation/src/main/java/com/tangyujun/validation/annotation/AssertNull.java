package com.tangyujun.validation.annotation;


import java.lang.annotation.*;

/**
 * 断言为空，任意类型
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertNull {
	/**
	 * 提示信息
	 */
	String message() default "required null";
}
