package com.tangyujun.delines.decoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DecoderFactory {

	private static final Map<Class<?>, IDelinesDecoder> decoders = new ConcurrentHashMap<>();

	static {
		decoders.put(SimpleDecoder.class, SimpleDecoder.getInstance());
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
	 * @param <T>   解析器类型
	 * @return 解析器实例
	 */
	public static <T extends IDelinesDecoder> T get(Class<T> clazz) {
		IDelinesDecoder decoder = decoders.get(clazz);
		if (decoder != null) {
			return (T) decoder;
		}
		try {
			T t = EntityFactory.get(clazz);
			decoders.put(clazz, t);
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}