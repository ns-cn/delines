package cn.tangyujun.delines.decoder;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * 简单类型自动推断，聚合类，包含简单的数据类型和简单的时间类型
 */
public class SimpleDecoder implements IDelinesDecoder, IDelinesDecoder.ExceptionHandler {


	private static final SimpleDecoder INSTANCE = new SimpleDecoder();

	public static SimpleDecoder getInstance() {
		return INSTANCE;
	}
	@Override
	public void handle(Matcher matcher, Object entity, Field field, Throwable exception) {
	}

	@Override
	public <T> T decode(Matcher result, Class<T> targetClazz) {
		if (!result.find()) {
			return null;
		}
		String data = result.group();
		if(String.class.equals(targetClazz)){
			return (T) data;
		}else if (Integer.class.equals(targetClazz)) {
			return (T) Integer.valueOf(data);
		} else if (Long.class.equals(targetClazz)) {
			return (T) Long.valueOf(data);
		} else if (Float.class.equals(targetClazz)) {
			return (T) Float.valueOf(data);
		} else if (Double.class.equals(targetClazz)) {
			return (T) Double.valueOf(data);
		} else if (Byte.class.equals(targetClazz)) {
			return (T) Byte.valueOf(data);
		} else if (LocalDateTime.class.equals(targetClazz) || LocalDate.class.equals(targetClazz)
				|| LocalTime.class.equals(targetClazz) || Date.class.equals(targetClazz)) {
			return SimpleDateDecoder.getInstance().decode(result, targetClazz);
		}
		throw new UnsupportedOperationException("unsupported " + targetClazz + " using " + SimpleDecoder.class);
	}
}
