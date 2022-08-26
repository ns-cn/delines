package com.tangyujun.delines.validation.validator;

import cn.hutool.core.text.CharSequenceUtil;
import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.StringNotEmpty;

import java.util.Optional;

public class StringNotEmptyValidator implements IInnerValidator<StringNotEmpty> {

	@Override
	public ValidationResult validate(Object data, StringNotEmpty required) {
		boolean success = Optional.ofNullable(data)
				.map(Object::toString)
				.map(CharSequenceUtil::isNotEmpty)
				.orElse(false);
		return success ? ValidationResult.ok() : ValidationResult.fail(required.message());
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}
