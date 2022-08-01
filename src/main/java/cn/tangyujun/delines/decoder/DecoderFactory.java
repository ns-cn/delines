package cn.tangyujun.delines.decoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DecoderFactory {

	private static final Map<Class<?>, IDelinesDecoder> decoders = new ConcurrentHashMap<>();

	static {
		decoders.put(SimpleDecoder.class, SimpleDecoder.getInstance());
		decoders.put(SimpleDateDecoder.class, SimpleDateDecoder.getInstance());
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
			T t = clazz.newInstance();
			decoders.put(clazz, t);
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
