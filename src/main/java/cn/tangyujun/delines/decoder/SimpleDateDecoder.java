package cn.tangyujun.delines.decoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * 简单日期类型的解析器
 */
public class SimpleDateDecoder implements IDelinesDecoder {

	private static final SimpleDateDecoder INSTANCE = new SimpleDateDecoder();
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static SimpleDateDecoder getInstance() {
		return INSTANCE;
	}

	@Override
	public <T> T decode(Matcher result, Class<T> targetClazz) {
		if (!result.find()) {
			return null;
		}
		String data = result.group();
		if (LocalDateTime.class.equals(targetClazz)) {
			return (T) LocalDateTime.parse(data);
		} else if (LocalTime.class.equals(targetClazz)) {
			return (T) LocalTime.parse(data);
		} else if (LocalDate.class.equals(targetClazz)) {
			return (T) LocalDate.parse(data);
		} else if (Date.class.equals(targetClazz)) {
			try {
				return (T) FORMAT.parse(data);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		throw new UnsupportedOperationException("unsupported " + targetClazz + " using " + SimpleDateDecoder.class);
	}
}
