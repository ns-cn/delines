package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.AssertFalse;

import java.util.Optional;

public class BooleanAssertFalseValidator implements IInnerValidator<AssertFalse> {

	@Override
	public ValidationResult validate(Object data, AssertFalse required) {
		boolean success = Optional.ofNullable(data)
				.map(Boolean.FALSE::equals)
				.orElse(false);
		return success ? ValidationResult.ok() : ValidationResult.fail(required.message());
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}
