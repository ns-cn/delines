package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.AssertNotNull;

import java.util.Objects;

/**
 * 对象非空校验器
 */
public class ObjectNotNullValidator implements IInnerValidator<AssertNotNull> {

	@Override
	public ValidationResult validate(Object data, AssertNotNull required) {
		return !Objects.isNull(data) ? ValidationResult.ok() : ValidationResult.fail(required.message());
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}
