package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.StringPattern;

import java.util.Optional;
import java.util.regex.Pattern;

public class StringPatternValidator implements IInnerValidator<StringPattern> {

	@Override
	public ValidationResult validate(Object data, StringPattern required) {
		Pattern pattern = Pattern.compile(required.value());
		boolean success = Optional.ofNullable(data)
				.map(Object::toString)
				.map(t -> pattern.matcher(t).find())
				.orElse(false);
		return success ? ValidationResult.ok() : ValidationResult.fail(required.message());
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}