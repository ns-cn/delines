package com.tangyujun.delines.decoder;

import com.tangyujun.delines.handler.NoneHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的转换异常处理器获取工厂<br>
 * 1。从已有的处理器中获取，
 * 2。根据类类型反射生成新的实体，并放到已有的实体中缓存
 */
public final class DefaultDecodeExceptionHandlerFactory implements IDecoderExceptionHandlerFactory {
	/**
	 * 已有的异常处理器
	 */
	private static final Map<Class<?>, IDelinesDecoder.ExceptionHandler> handlers = new ConcurrentHashMap<>();

	static {
		handlers.put(NoneHandler.class, new NoneHandler());
	}

	/**
	 * 默认的转换异常处理器工厂类
	 */
	public DefaultDecodeExceptionHandlerFactory() {
	}

	/**
	 * 注册具体类型
	 *
	 * @param clazz 类型
	 * @param t     值
	 * @param <T>   具体类型
	 */
	public static <T extends IDelinesDecoder.ExceptionHandler> void register(Class<T> clazz, T t) {
		handlers.put(clazz, t);
	}

	/**
	 * 注销具体类型
	 *
	 * @param clazz 类型
	 * @param <T>   类型泛型
	 */
	public static <T extends IDelinesDecoder.ExceptionHandler> void deregister(Class<T> clazz) {
		handlers.remove(clazz);
	}

	/**
	 * 获取类型对应的异常处理器
	 *
	 * @param clazz 类类型
	 * @return 类型对应的异常处理器
	 */
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
