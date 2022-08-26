package com.tangyujun.delines.validation.annotation;

import com.tangyujun.delines.validation.IValidator;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidation {

	/**
	 * 实际校验内容
	 */
	Class<? extends IValidator> validator();
}
