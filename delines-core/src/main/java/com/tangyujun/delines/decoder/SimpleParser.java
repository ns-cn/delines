package com.tangyujun.delines.decoder;

import cn.hutool.core.lang.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;
import java.util.Optional;
import java.util.function.Function;

public class SimpleParser {

	public static Object LocalDateTime(Object formatter, String data) {
		return withDateFormatter(formatter, data, LocalDateTime::from, LocalDateTime::parse);
	}

	public static Object LocalDate(Object formatter, String data) {
		return withDateFormatter(formatter, data, LocalDate::from, LocalDate::parse);
	}

	public static Object LocalTime(Object formatter, String data) {
		return withDateFormatter(formatter, data, LocalTime::from, LocalTime::parse);
	}

	public static Object Date(Object formatter, String data) {
		Assert.isInstanceOf(SimpleDateFormat.class, formatter);
		try {
			return ((SimpleDateFormat) formatter).parse(data);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}


	private static <T> Object withDateFormatter(Object formatter, String data, TemporalQuery<T> query,
	                                            Function<String, Object> defaultParser) {
		if (formatter != null) {
			Assert.isInstanceOf(DateTimeFormatter.class, formatter);
		}
		return Optional.ofNullable(formatter)
				.map(t -> (DateTimeFormatter) t)
				.map(t -> (Object) t.parse(data, query))
				.orElse(defaultParser.apply(data));
	}
}
