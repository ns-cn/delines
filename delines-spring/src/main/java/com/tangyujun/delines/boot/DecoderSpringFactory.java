package com.tangyujun.delines.boot;

import cn.hutool.extra.spring.SpringUtil;
import com.tangyujun.delines.decoder.IDecoderFactory;
import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.decoder.IEntityFactory;

public class DecoderSpringFactory implements IDecoderFactory {
	@Override
	public IDelinesDecoder get(Class<? extends IDelinesDecoder> clazz) {
		IDelinesDecoder bean = SpringUtil.getBean(clazz);
		if (bean == null) {
			return IEntityFactory.build(clazz);
		}
		return bean;
	}
}
