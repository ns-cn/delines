package com.tangyujun.delines.validation.factories;

import cn.hutool.extra.spring.SpringUtil;
import com.tangyujun.delines.decoder.IEntityFactory;
import com.tangyujun.delines.validation.IValidator;

public class SpringValidatorFactory implements IEntityFactory<IValidator> {
	@Override
	public IValidator get(Class<? extends IValidator> clazz) {
		IValidator bean = SpringUtil.getBean(clazz);
		if (bean == null) {
			return IEntityFactory.build(clazz);
		}
		return bean;
	}
}
