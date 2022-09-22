package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.AssertNull;

import java.util.Objects;

/**
 * 对象为空校验器
 */
public class ObjectNullValidator implements IInnerValidator<AssertNull> {

	@Override
	public ValidationResult validate(Object data, AssertNull required) {
		return Objects.isNull(data) ? ValidationResult.ok() : ValidationResult.fail(required.message());
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}
