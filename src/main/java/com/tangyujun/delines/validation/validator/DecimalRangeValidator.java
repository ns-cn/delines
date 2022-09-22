package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.annotation.DecimalRange;
import com.tangyujun.delines.validation.ValidationResult;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class DecimalRangeValidator extends DecimalValidator<DecimalRange> {

	@Override
	public ValidationResult validate(Object data, DecimalRange required) {
		if (Objects.isNull(data)) {
			return ValidationResult.fail(required.message());
		}
		String min = required.min();
		String max = required.max();
		String message = required.message();
		ValidationResult minResult = null, maxResult = null;
		if (data instanceof Integer) {
			minResult = validate((Integer) data, min, message, Integer::parseInt, (i1, i2) -> i1 >= i2);
			maxResult = validate((Integer) data, max, message, Integer::parseInt, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Long) {
			minResult = validate((Long) data, min, message, Long::parseLong, (i1, i2) -> i1 >= i2);
			maxResult = validate((Long) data, max, message, Long::parseLong, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Short) {
			minResult = validate((Short) data, min, message, Short::parseShort, (i1, i2) -> i1 >= i2);
			maxResult = validate((Short) data, max, message, Short::parseShort, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Byte) {
			minResult = validate((Byte) data, min, message, Byte::parseByte, (i1, i2) -> i1 >= i2);
			maxResult = validate((Byte) data, max, message, Byte::parseByte, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Double) {
			minResult = validate((Double) data, min, message, Double::parseDouble, (i1, i2) -> i1 >= i2);
			maxResult = validate((Double) data, max, message, Double::parseDouble, (i1, i2) -> i1 <= i2);
		} else if (data instanceof Float) {
			minResult = validate((Float) data, min, message, Float::parseFloat, (i1, i2) -> i1 >= i2);
			maxResult = validate((Float) data, max, message, Float::parseFloat, (i1, i2) -> i1 <= i2);
		} else if (data instanceof BigInteger) {
			minResult = validate((BigInteger) data, min, message, BigInteger::new, (i1, i2) -> i1.compareTo(i2) >= 0);
			maxResult = validate((BigInteger) data, max, message, BigInteger::new, (i1, i2) -> i1.compareTo(i2) <= 0);
		} else if (data instanceof BigDecimal) {
			minResult = validate((BigDecimal) data, min, message, BigDecimal::new, (i1, i2) -> i1.compareTo(i2) >= 0);
			maxResult = validate((BigDecimal) data, max, message, BigDecimal::new, (i1, i2) -> i1.compareTo(i2) <= 0);
		}
		if (minResult != null && maxResult != null) {
			return ValidationResult.merge(minResult, maxResult).isFail() ? ValidationResult.fail(required.message()) :
					ValidationResult.ok();
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
