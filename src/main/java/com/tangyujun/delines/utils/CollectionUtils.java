package com.tangyujun.delines.utils;

import java.util.*;

/**
 * 集合类型的工具类
 */
public final class CollectionUtils {

	/**
	 * 构建list
	 *
	 * @param clazz 类型
	 * @param <T>   类型泛型
	 * @return 集合类型
	 */
	public static <T> List<T> newList(Class<T> clazz) {
		return new ArrayList<>();
	}

	/**
	 * 构建set
	 *
	 * @param clazz 类型
	 * @param <T>   类型泛型
	 * @return 集合类型
	 */
	public static <T> Set<T> newSet(Class<T> clazz) {
		return new HashSet<>();
	}

	/**
	 * 构建集合类型
	 *
	 * @param clazz       类型
	 * @param <T>         类型泛型
	 * @param collectType 具体的集合类型
	 * @return 集合类型
	 */
	public static <T> Collection<T> newCollection(Class<T> clazz, Class<T> collectType) {
		return List.class.equals(collectType) ? newList(clazz) : newSet(clazz);
	}
}
