package cn.tangyujun.delines.decoder;

import cn.tangyujun.delines.exceptionhandler.NoneHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DecodeExceptionHandlerFactory {
	private static final Map<Class<?>, IDelinesDecoder.ExceptionHandler> handlers = new ConcurrentHashMap<>();

	static {
		handlers.put(NoneHandler.class, new NoneHandler());
	}

	public static <T extends IDelinesDecoder.ExceptionHandler> void register(Class<T> clazz, T t) {
		handlers.put(clazz, t);
	}

	public static <T extends IDelinesDecoder.ExceptionHandler> void deregister(Class<T> clazz) {
		handlers.remove(clazz);
	}

	public static <T extends IDelinesDecoder.ExceptionHandler> T get(Class<T> clazz) {
		IDelinesDecoder.ExceptionHandler handler = handlers.get(clazz);
		if (handler != null) {
			return (T) handler;
		}
		try {
			T t = clazz.newInstance();
			handlers.put(clazz, t);
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
