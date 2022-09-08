package com.tangyujun.delines.parser;

import java.util.*;

public final class CollectionUtils {

	public static <T> List<T> newList(Class<T> clazz) {
		return new ArrayList<>();
	}

	public static <T> Set<T> newSet(Class<T> clazz) {
		return new HashSet<>();
	}

	public static <T> Collection<T> newCollection(Class<T> clazz, Class<T> collectType) {
		return List.class.equals(collectType) ? newList(clazz) : newSet(clazz);
	}
}
