package com.tangyujun.delines.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 对象比较工具，并通过字符串标识结果差异
 */
public final class CompareUtils {

	/**
	 * 对比俩对象的toString结果
	 *
	 * @param obj  第一个对象
	 * @param obj2 第二个对象
	 * @param <T>  对象类型
	 * @return 对比结果字符串
	 */
	public static <T> String compare(T obj, T obj2) {
		return compare(obj, obj2, t -> t, Object::toString);
	}

	/**
	 * 给定两个类型相同的对象进行比较制定的字段，并通过两个对象的equals方法对比，通过toString显示差异
	 *
	 * @param obj    第一个对象
	 * @param obj2   第二个对象
	 * @param mapper 对象对比内容的获取
	 * @param <T>    对象类型
	 * @param <F>    字段类型
	 * @return 对比结果字符串
	 */
	public static <T, F> String compare(T obj, T obj2, Function<T, F> mapper) {
		return compare(obj, obj2, mapper, Object::toString);
	}

	/**
	 * 给定两个相同类型的对象对比指定的字段，并通过制定的显示方法显示差异
	 *
	 * @param obj     第一个对象
	 * @param obj2    第二个对象
	 * @param mapper  字段获取方式
	 * @param display 显示内容
	 * @param <T>     对象类型
	 * @param <F>     字段类型
	 * @return 对比结果字符串
	 */
	public static <T, F> String compare(T obj, T obj2, Function<T, F> mapper, Function<F, String> display) {
		return compare(obj, obj2, mapper, mapper, display);
	}

	/**
	 * 给两个不同类型的对象对比各自指定的字段，并通过指定的显示方法显示差异
	 *
	 * @param t1       第一个对象
	 * @param t2       第二个对象
	 * @param t1Mapper 具体对比内容的获取mapper
	 * @param t2Mapper 具体对比内容的获取mapper
	 * @param display  对比内容不同时的对象显示方式定义
	 * @param <T1>     第一个类型
	 * @param <T2>     第二个类型
	 * @param <F>      对比类型
	 * @return 对比结果字符串
	 */
	public static <T1, T2, F> String compare(T1 t1, T2 t2, Function<T1, F> t1Mapper, Function<T2, F> t2Mapper,
	                                         Function<F, String> display) {
		Objects.requireNonNull(t1Mapper);
		Objects.requireNonNull(t2Mapper);
		Objects.requireNonNull(display);
		Function<F, String> packageDisplay = f -> Optional.ofNullable(f)
				.map(display)
				.orElse("");
		F value1 = Optional.ofNullable(t1)
				.map(t1Mapper)
				.orElse(null);
		F value2 = Optional.ofNullable(t2)
				.map(t2Mapper)
				.orElse(null);
		if (!Objects.equals(value1, value2)) {
			return String.format("%s→%s", packageDisplay.apply(value1), packageDisplay.apply(value2));
		}
		return null;
	}
}

