package com.tangyujun.delines.validator;

import com.tangyujun.delines.DelinesBusEntity;
import com.tangyujun.delines.DelinesBusField;

import java.util.Objects;
import java.util.Optional;

@FunctionalInterface
public interface DelinesValidator {
	ValidatorResult check(DelinesBusEntity<?> entity, DelinesBusField field, Object value);

	/**
	 * 不做校验
	 */
	class None implements DelinesValidator{
		@Override
		public ValidatorResult check(DelinesBusEntity<?> entity, DelinesBusField field, Object value) {
			return null;
		}
	}
	/**
	 * 非空校验
	 */
	class NotNull implements DelinesValidator {
		@Override
		public ValidatorResult check(DelinesBusEntity<?> entity, DelinesBusField field, Object value) {
			String name = field.getField().getName();
			return Optional.ofNullable(value).map(t -> ValidatorResult.ok())
					.orElse(ValidatorResult.fail(name + "could not be null"));
		}
	}
}
