package com.tangyujun.delines.decoder;

import com.tangyujun.delines.DelinesBusField;

import java.util.*;
import java.util.regex.Matcher;

/**
 * 对象的Set集合的默认解析器
 *
 * @param <T> 具体的解析类型
 */
public abstract class SetObjectDecoder<T> implements IDelinesDecoder {
	@Override
	public Object decode(Matcher result, DelinesBusField field) {
		Set<T> results = new HashSet<>();
		if (!result.find()) {
			return null;
		}
		do {
			Optional.ofNullable(decode(result.group(), field))
					.ifPresent(results::add);
		} while (result.find());
		return results;
	}

	/**
	 * 具体字符串解析为对应类型的解析器
	 *
	 * @param data  字符串数据
	 * @param field 对应的字段解析对象
	 * @return 解析后的数据
	 */
	public abstract T decode(String data, DelinesBusField field);
}
