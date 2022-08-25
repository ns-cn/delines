package com.tangyujun.delines.spring;

import cn.hutool.extra.spring.SpringUtil;
import com.tangyujun.delines.decoder.IDecoderExceptionHandlerFactory;
import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.decoder.IEntityFactory;

/**
 * 从Spring中获取Bean的工厂类
 */
public class DecodeExceptionHandlerSpringFactory implements IDecoderExceptionHandlerFactory {
	@Override
	public IDelinesDecoder.ExceptionHandler get(Class<? extends IDelinesDecoder.ExceptionHandler> clazz) {
		IDelinesDecoder.ExceptionHandler bean = SpringUtil.getBean(clazz);
		if (bean == null) {
			return IEntityFactory.build(clazz);
		}
		return bean;
	}
}