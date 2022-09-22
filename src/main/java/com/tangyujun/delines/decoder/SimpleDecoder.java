package com.tangyujun.delines.decoder;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import com.tangyujun.delines.DelinesBusField;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;

/**
 * 简单类型自动推断，聚合类，包含简单的数据类型和简单的时间类型
 */
public class SimpleDecoder implements IDelinesDecoder, IDelinesDecoder.ExceptionHandler {

	/**
	 * 默认解析器
	 */
	private static final SimpleDecoder INSTANCE = new SimpleDecoder();

	/**
	 * 默认时间格式
	 */
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取单例实体
	 *
	 * @return 实体
	 */
	public static SimpleDecoder getInstance() {
		return INSTANCE;
	}

	/**
	 * 单个类型对应的字符串转实体的function
	 */
	private static final Map<Class<?>, Function<String, Object>> functionMap = new HashMap<>();
	/**
	 * 类型对应的字符串转实体的function,带格式参数
	 */
	private static final Map<Class<?>, BiFunction<Object, String, Object>> biFunctionMap = new HashMap<>();

	/**
	 * 注册默认的function
	 */
	static {
		functionMap.put(String.class, data -> data);
		functionMap.put(Integer.class, Integer::parseInt);
		functionMap.put(Long.class, Long::parseLong);
		functionMap.put(Boolean.class, Boolean::parseBoolean);
		functionMap.put(Short.class, Short::parseShort);
		functionMap.put(Float.class, Float::parseFloat);
		functionMap.put(Double.class, Double::parseDouble);
		functionMap.put(Byte.class, Byte::parseByte);
		biFunctionMap.put(LocalDateTime.class, SimpleDecoder::LocalDateTime);
		biFunctionMap.put(LocalDate.class, SimpleDecoder::LocalDate);
		biFunctionMap.put(LocalTime.class, SimpleDecoder::LocalTime);
		biFunctionMap.put(Date.class, SimpleDecoder::Date);
	}

	@Override
	public void handle(Matcher matcher, Object entity, Field field, Throwable exception) {
	}

	/**
	 * 获取对应时间类型的formatter
	 *
	 * @param clazz  时间类 型
	 * @param format 格式化字符串
	 * @return 对应的formatter
	 */
	private Object formatter(Class<?> clazz, String format) {
		if (LocalDateTime.class.equals(clazz)
				|| LocalTime.class.equals(clazz)
				|| LocalDate.class.equals(clazz)) {
			return !CharSequenceUtil.isEmpty(format) ? DateTimeFormatter.ofPattern(format) : null;
		} else if (Date.class.equals(clazz)) {
			return !CharSequenceUtil.isEmpty(format) ? new SimpleDateFormat(format) : FORMAT;
		}
		return null;
	}

	@Override
	public Object decode(Matcher result, DelinesBusField field) {
		if (!result.find()) {
			return null;
		}
		Class<?> targetClazz = field.getResultType();
		String data = result.group();
		String format = field.getDateFormat();
		Object formatter = formatter(targetClazz, format);

		Function<String, Object> function = functionMap.get(targetClazz);
		if (function != null) {
			return function.apply(data);
		}
		BiFunction<Object, String, Object> biFunction = biFunctionMap.get(targetClazz);
		if (biFunction != null) {
			return biFunction.apply(formatter, data);
		}
		if (List.class.equals(targetClazz) || Set.class.equals(targetClazz)) {
			Type[] actualTypeArguments = ((ParameterizedType) field.getField().getGenericType()).getActualTypeArguments();
			if (actualTypeArguments.length != 1) {
				throw new RuntimeException("type assignment required!");
			}
			try {
				Class<?> innerType = Class.forName(actualTypeArguments[0].getTypeName());
				Object innerFormatter = formatter(innerType, format);
				Collection<Object> results = new ArrayList<>();
				if (Set.class.equals(targetClazz)) {
					results = new HashSet<>();
				}
				do {
					data = result.group();
					Function<String, Object> innerFunction = functionMap.get(innerType);
					if (innerFunction != null) {
						results.add(innerFunction.apply(data));
						continue;
					}
					BiFunction<Object, String, Object> innerBiFunction = biFunctionMap.get(innerType);
					if (innerBiFunction != null) {
						results.add(innerBiFunction.apply(innerFormatter, data));
						continue;
					}
					throw new RuntimeException("not supported inner type:" + innerType);
				} while (result.find());
				return results;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		throw new UnsupportedOperationException("unsupported " + targetClazz + " using " + SimpleDecoder.class);
	}

	/**
	 * localdatetime类型的转换方法
	 *
	 * @param formatter localdatetime的转换formatter
	 * @param data      字符串
	 * @return 转换后的localDateTime
	 */
	public static Object LocalDateTime(Object formatter, String data) {
		return withDateFormatter(formatter, data, LocalDateTime::from, LocalDateTime::parse);
	}

	/**
	 * localdate类型的转换方法
	 *
	 * @param formatter localdate的转换formatter
	 * @param data      字符串
	 * @return 转换后的localdate
	 */
	public static Object LocalDate(Object formatter, String data) {
		return withDateFormatter(formatter, data, LocalDate::from, LocalDate::parse);
	}

	/**
	 * localtime类型的转换方法
	 *
	 * @param formatter localtime的转换formatter
	 * @param data      字符串
	 * @return 转换后的localtime
	 */
	public static Object LocalTime(Object formatter, String data) {
		return withDateFormatter(formatter, data, LocalTime::from, LocalTime::parse);
	}

	/**
	 * date类型的转换方法
	 *
	 * @param formatter date的转换formatter
	 * @param data      字符串
	 * @return 转换后的date
	 */
	public static Object Date(Object formatter, String data) {
		Assert.isInstanceOf(SimpleDateFormat.class, formatter);
		try {
			return ((SimpleDateFormat) formatter).parse(data);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用formatter转换成对应的时间类型
	 *
	 * @param formatter     formatter
	 * @param data          字符串
	 * @param query         TemporalQuery
	 * @param defaultParser 默认的转换方式
	 * @param <T>           时间类型
	 * @return 转换后的时间类型
	 */
	private static <T> Object withDateFormatter(Object formatter, String data, TemporalQuery<T> query,
	                                            Function<String, Object> defaultParser) {
		if (formatter != null) {
			Assert.isInstanceOf(DateTimeFormatter.class, formatter);
		}
		if (formatter != null) {
			return ((DateTimeFormatter) formatter).parse(data, query);
		}
		return defaultParser.apply(data);
	}
}
