package com.tangyujun.delines.decoder;

import com.tangyujun.delines.annotation.EntityCreator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 实体工厂接口
 *
 * @param <T> 类型泛型
 */
public interface IEntityFactory<T> {

	/**
	 * 构建一个指定类型的实体
	 *
	 * @param clazz 类型
	 * @param <T>   泛型类型
	 * @return 指定类型的实体
	 */
	static <T> T build(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		Method[] methods = clazz.getDeclaredMethods();
		Method creator = null;
		for (Method method : methods) {
			EntityCreator entityCreator = method.getAnnotation(EntityCreator.class);
			if (entityCreator != null) {
				if (creator != null) {
					throw new RuntimeException("@EntityCreator can be only used once in each class");
				}
				if (!Modifier.isStatic(method.getModifiers())
						|| !Modifier.isPublic(method.getModifiers())
						|| method.getParameterCount() != 0
						|| method.getReturnType() != clazz) {
					throw new RuntimeException("@EntityCreator can be used on only zero-args" +
							" public static method with returning class itself");
				}
				creator = method;
			}
		}
		if (creator != null) {
			try {
				T t = (T) creator.invoke(null, new Object[]{});
				if (t == null) {
					throw new RuntimeException("annotated @EntityCreator but returning null");
				}
				return t;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		Constructor<?>[] constructors = clazz.getConstructors();
		try {
			return clazz.newInstance();
		} catch (Exception exception) {
			for (Constructor<?> constructor : constructors) {
				if (constructor.getParameterCount() == 0) {
					constructor.setAccessible(true);
					try {
						return (T) constructor.newInstance();
					} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		throw new RuntimeException("now constructor or @EntityCreator zero-args public static method declared\n" +
				"class:" + clazz);
	}

	/**
	 * 根据类类型获取类类型实例
	 *
	 * @param clazz 类类型
	 * @return 获取类类型实例
	 */
	T get(Class<? extends T> clazz);
}
