package com.tangyujun.delines.decoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultDecoderFactory implements IDecoderFactory {

	private static final Map<Class<?>, IDelinesDecoder> decoders = new ConcurrentHashMap<>();

	static {
		decoders.put(SimpleDecoder.class, SimpleDecoder.getInstance());
	}

	public DefaultDecoderFactory() {
	}

	public static <T extends IDelinesDecoder> void register(Class<T> clazz, T t) {
		decoders.put(clazz, t);
	}

	public static <T extends IDelinesDecoder> void deregister(Class<T> clazz) {
		decoders.remove(clazz);
	}

	/**
	 * 获取目标类型的解析器实例
	 *
	 * @param clazz 解析器的类型
	 * @return 解析器实例
	 */
	@Override
	public IDelinesDecoder get(Class<? extends IDelinesDecoder> clazz) {
		IDelinesDecoder decoder = decoders.get(clazz);
		if (decoder != null) {
			return decoder;
		}
		try {
			decoder = IEntityFactory.build(clazz);
			decoders.put(clazz, decoder);
			return decoder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
