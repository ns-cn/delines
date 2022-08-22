package com.tangyujun.delines.validator;

import com.tangyujun.delines.DelinesBusEntity;
import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.IDelinesEntity;

import java.util.Objects;
import java.util.Optional;

@FunctionalInterface
public interface DelinesValidator<T extends IDelinesEntity> {
	ValidatorResult  check(DelinesBusEntity<T> entity, T value);

	/**
	 * 不做校验
	 */
	class None implements DelinesValidator<IDelinesEntity>{
		@Override
		public ValidatorResult check(DelinesBusEntity<IDelinesEntity> entity, IDelinesEntity value) {
			return null;
		}
	}
}
