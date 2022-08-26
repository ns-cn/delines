package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.AssertNotNull;
import com.tangyujun.delines.validation.annotation.DecimalMax;
import com.tangyujun.delines.validation.annotation.DecimalMin;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class DecimalMaxValidator extends DecimalValidator<DecimalMax> {

	@Override
	public ValidationResult validate(Object data, DecimalMax required) {
		if (Objects.isNull(data)) {
			return ValidationResult.fail(required.message());
		}
		String value = required.value();
		String message = required.message();
		if (data instanceof Integer) {
			return validate((Integer) data, value, message, Integer::parseInt, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Long) {
			return validate((Long) data, value, message, Long::parseLong, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Short) {
			return validate((Short) data, value, message, Short::parseShort, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Byte) {
			return validate((Byte) data, value, message, Byte::parseByte, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Double) {
			return validate((Double) data, value, message, Double::parseDouble, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Float) {
			return validate((Float) data, value, message, Float::parseFloat, (i1, i2) -> i1 <= i2);
		} else if (data instanceof BigInteger) {
			return validate((BigInteger) data, value, message, BigInteger::new, (i1, i2) -> i1.compareTo(i2) <= 0);
		} else if (data instanceof BigDecimal) {
			return validate((BigDecimal) data, value, message, BigDecimal::new, (i1, i2) -> i1.compareTo(i2) <= 0);
		}
		try {
			throw new OperationNotSupportedException();
		} catch (OperationNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}

}
