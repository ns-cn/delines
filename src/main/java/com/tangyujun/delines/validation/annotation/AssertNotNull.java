package com.tangyujun.delines.validation.annotation;


import java.lang.annotation.*;

/**
 * 断言为非空，任意类型
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertNotNull {
	/**
	 * @return 提示信息
	 */
	String message() default "required not null";
}
