package com.tangyujun.delines.decoder;

import com.tangyujun.delines.DelinesBusField;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * 简单类型自动推断，聚合类，包含简单的数据类型和简单的时间类型
 */
public class SimpleDecoder implements IDelinesDecoder, IDelinesDecoder.ExceptionHandler {


	private static final SimpleDecoder INSTANCE = new SimpleDecoder();

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDecoder getInstance() {
		return INSTANCE;
	}

	@Override
	public void handle(Matcher matcher, Object entity, Field field, Throwable exception) {
	}

	@Override
	public Object decode(Matcher result, DelinesBusField field) {
		if (!result.find()) {
			return null;
		}
		Class<?> targetClazz = field.getResultType();
		String data = result.group();
		String format = field.getDateFormat();
		boolean specificFormat = format == null || format.equals("");
		if (String.class.equals(targetClazz)) {
			return data;
		} else if (Integer.class.equals(targetClazz)) {
			return Integer.valueOf(data);
		} else if (Long.class.equals(targetClazz)) {
			return Long.valueOf(data);
		} else if (Boolean.class.equals(targetClazz)) {
			return Boolean.valueOf(data);
		} else if (Float.class.equals(targetClazz)) {
			return Float.valueOf(data);
		} else if (Double.class.equals(targetClazz)) {
			return Double.valueOf(data);
		} else if (Byte.class.equals(targetClazz)) {
			return Byte.valueOf(data);
		} else if (LocalDateTime.class.equals(targetClazz)) {
			return specificFormat ? LocalDateTime.parse(data) : LocalDateTime.parse(data, DateTimeFormatter.ofPattern(format));
		} else if (LocalTime.class.equals(targetClazz)) {
			return specificFormat ? LocalTime.parse(data) : LocalTime.parse(data, DateTimeFormatter.ofPattern(format));
		} else if (LocalDate.class.equals(targetClazz)) {
			return specificFormat ? LocalDate.parse(data) : LocalDate.parse(data, DateTimeFormatter.ofPattern(format));
		} else if (Date.class.equals(targetClazz)) {
			try {
				return specificFormat ? FORMAT.parse(data) : new SimpleDateFormat(format).parse(data);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		throw new UnsupportedOperationException("unsupported " + targetClazz + " using " + SimpleDecoder.class);
	}
}
