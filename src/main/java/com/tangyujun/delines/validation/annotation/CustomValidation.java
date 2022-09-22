package com.tangyujun.delines.validation.annotation;

import com.tangyujun.delines.validation.IValidator;

import java.lang.annotation.*;

/**
 * 可注解在类上或字段上的自定义注解类型
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidation {

	/**
	 * 实际校验方法
	 */
	Class<? extends IValidator> validator();
}
