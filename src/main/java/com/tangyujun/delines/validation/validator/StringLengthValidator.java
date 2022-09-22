package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.StringLength;

import java.util.Optional;

/**
 * 字符串长度校验器
 */
public class StringLengthValidator implements IInnerValidator<StringLength> {

	@Override
	public ValidationResult validate(Object data, StringLength required) {
		int size = Optional.ofNullable(data)
				.map(Object::toString)
				.map(String::length)
				.orElse(0);
		boolean success = size >= required.min() && size <= required.max();
		return success ? ValidationResult.ok() : ValidationResult.fail(required.message());
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}
