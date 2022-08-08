package com.tangyujun.delines.decoder;

import com.tangyujun.delines.exceptionhandler.NoneHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultDecodeExceptionHandlerFactory implements IDecoderExceptionHandlerFactory {
	private static final Map<Class<?>, IDelinesDecoder.ExceptionHandler> handlers = new ConcurrentHashMap<>();

	static {
		handlers.put(NoneHandler.class, new NoneHandler());
	}

	public DefaultDecodeExceptionHandlerFactory() {
	}

	public static <T extends IDelinesDecoder.ExceptionHandler> void register(Class<T> clazz, T t) {
		handlers.put(clazz, t);
	}

	public static <T extends IDelinesDecoder.ExceptionHandler> void deregister(Class<T> clazz) {
		handlers.remove(clazz);
	}

	public IDelinesDecoder.ExceptionHandler get(Class<? extends IDelinesDecoder.ExceptionHandler> clazz) {
		IDelinesDecoder.ExceptionHandler handler = handlers.get(clazz);
		if (handler != null) {
			return handler;
		}
		try {
			handler = IEntityFactory.build(clazz);
			handlers.put(clazz, handler);
			return handler;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
