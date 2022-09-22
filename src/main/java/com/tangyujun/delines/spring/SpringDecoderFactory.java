package com.tangyujun.delines.spring;

import cn.hutool.extra.spring.SpringUtil;
import com.tangyujun.delines.decoder.IDecoderFactory;
import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.decoder.IEntityFactory;
/**
 * 从Spring中获取Bean的工厂类
 */
public class SpringDecoderFactory implements IDecoderFactory {
	@Override
	public IDelinesDecoder get(Class<? extends IDelinesDecoder> clazz) {
		IDelinesDecoder bean = SpringUtil.getBean(clazz);
		if (bean == null) {
			return IEntityFactory.build(clazz);
		}
		return bean;
	}
}
