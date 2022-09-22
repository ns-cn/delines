package com.tangyujun.delines.decoder;

import com.tangyujun.delines.DelinesBusField;

import java.util.regex.Matcher;

/**
 * 单个对象的解析器
 *
 * @param <T> 具体解析类型
 */
public abstract class SingleObjectDecoder<T> implements IDelinesDecoder {

	@Override
	public Object decode(Matcher result, DelinesBusField field) {
		if (!result.find()) {
			return null;
		}
		return decode(result.group(), field);
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
