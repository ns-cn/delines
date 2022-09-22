package com.tangyujun.delines.decoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的解析器工厂
 */
public final class DefaultDecoderFactory implements IDecoderFactory {

	/**
	 * 类型与实际解析器的map
	 */
	private static final Map<Class<?>, IDelinesDecoder> decoders = new ConcurrentHashMap<>();

	/**
	 * 注册默认的通用解析器
	 */
	static {
		decoders.put(SimpleDecoder.class, SimpleDecoder.getInstance());
	}

	/**
	 * 默认的解析器工厂
	 */
	public DefaultDecoderFactory() {
	}

	/**
	 * 注册对应类型的解析器
	 *
	 * @param clazz 类型
	 * @param t     解析器实体
	 * @param <T>   类型泛型
	 */
	public static <T extends IDelinesDecoder> void register(Class<T> clazz, T t) {
		decoders.put(clazz, t);
	}

	/**
	 * 注销对应类型的解析器
	 *
	 * @param clazz 类型
	 * @param <T>   类型泛型
	 */
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
