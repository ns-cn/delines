package com.tangyujun.delines.validation.factories;

import com.tangyujun.delines.decoder.DefaultDecoderFactory;
import com.tangyujun.delines.decoder.IEntityFactory;
import com.tangyujun.delines.validation.IValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultValidatorFactory implements IEntityFactory<IValidator> {

	private final Map<Class<?>, IValidator> validators = new HashMap<>();

	public DefaultValidatorFactory() {
	}

	@Override
	public IValidator get(Class<? extends IValidator> clazz) {
		synchronized (DefaultDecoderFactory.class) {
			IValidator validator = validators.get(clazz);
			if (validator == null) {
				validator = IEntityFactory.build(clazz);
				Optional.ofNullable(validator).ifPresent(t -> validators.put(clazz, t));
				validators.put(clazz, validator);
			}
			return validator;
		}
	}
}
