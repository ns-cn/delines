package com.tangyujun.delines.validation.factories;

import com.tangyujun.delines.decoder.DefaultDecoderFactory;
import com.tangyujun.delines.decoder.IEntityFactory;
import com.tangyujun.delines.validation.IValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 默认的校验器工厂
 */
public class DefaultValidatorFactory implements IEntityFactory<IValidator> {

	/**
	 * 校验器map,类型与具体校验器的映射
	 */
	private final Map<Class<?>, IValidator> validators = new HashMap<>();

	/**
	 * 默认校验器工厂类
	 */
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
