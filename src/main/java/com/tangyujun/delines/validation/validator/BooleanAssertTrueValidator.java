package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.annotation.AssertTrue;
import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;

import java.util.Optional;

public class BooleanAssertTrueValidator implements IInnerValidator<AssertTrue> {

	@Override
	public ValidationResult validate(Object data, AssertTrue required) {
		boolean success = Optional.ofNullable(data)
				.map(Boolean.TRUE::equals)
				.orElse(false);
		return success ? ValidationResult.ok() : ValidationResult.fail(required.message());
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}
