package com.tangyujun.delines.parser;

import com.tangyujun.delines.DelinesBusEntity;
import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.IDelinesEntity;
import com.tangyujun.delines.annotation.DelinesEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public final class DelinesEntityParser {

	public static final DelinesEntityParser DEFAULT = new DelinesEntityParser();

	private DelinesFieldParser fieldParser;

	private DelinesEntityParser() {
		fieldParser = DelinesFieldParser.DEFAULT;
	}

	private DelinesEntityParser(DelinesFieldParser fieldParser) {
		this.setFieldParser(fieldParser);
	}

	public static <T extends IDelinesEntity> DelinesBusEntity<T> parseUsingDefault(Class<T> clazz) {
		return DEFAULT.parse(clazz);
	}

	public static DelinesEntityParser custom() {
		return new DelinesEntityParser();
	}

	public void setFieldParser(DelinesFieldParser fieldParser) {
		Objects.requireNonNull(fieldParser, "could not build a DelinesEntityParser without DelinesFieldParser");
		this.fieldParser = fieldParser;
	}

	public DelinesEntityParser withFieldParser(DelinesFieldParser fieldParser) {
		setFieldParser(fieldParser);
		return this;
	}

	public <T extends IDelinesEntity> DelinesBusEntity<T> parse(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		List<DelinesBusField> delinesBusFields = new ArrayList<>();
		DelinesBusEntity<T> bus = DelinesBusEntity.of(clazz, delinesBusFields);
		DelinesEntity delinesEntity = clazz.getAnnotation(DelinesEntity.class);
		if (delinesEntity != null) {
			String required = delinesEntity.required();
			DelinesEntity.RangeType rangeStartType = delinesEntity.rangeStartType();
			DelinesEntity.RangeType rangeEndType = delinesEntity.rangeEndType();
			if (required != null && !required.equals("")) {
				bus.setRequired(Pattern.compile(required));
			}
			bus.setRangeStartType(Optional.ofNullable(delinesEntity.rangeStartType()).orElse(DelinesEntity.RangeType.NONE));
			bus.setRangeStartBorder(delinesEntity.rangeStartBorder());
			bus.setRangeEndType(Optional.ofNullable(delinesEntity.rangeEndType()).orElse(DelinesEntity.RangeType.NONE));
			bus.setRangeEndBorder(delinesEntity.rangeEndBorder());
			if (DelinesEntity.RangeType.NUMBER.equals(rangeStartType)) {
				if (DelinesEntity.RangeBorder.EXCLUDE.equals(delinesEntity.rangeStartBorder())) {
					bus.setRangeStartLine(Long.parseLong(delinesEntity.rangeStart()) + 1);
				} else {
					bus.setRangeStartLine(Long.valueOf(delinesEntity.rangeStart()));
				}
			} else if (DelinesEntity.RangeType.REGULAR.equals(rangeStartType)) {
				bus.setRangeStartPattern(Pattern.compile(delinesEntity.rangeStart()));
			}
			if (DelinesEntity.RangeType.NUMBER.equals(rangeEndType)) {
				if (DelinesEntity.RangeBorder.EXCLUDE.equals(delinesEntity.rangeEndBorder())) {
					bus.setRangeEndLine(Long.parseLong(delinesEntity.rangeEnd()) - 1);
				} else {
					bus.setRangeEndLine(Long.valueOf(delinesEntity.rangeEnd()));
				}
			} else if (DelinesEntity.RangeType.REGULAR.equals(rangeEndType)) {
				bus.setRangeEndPattern(Pattern.compile(delinesEntity.rangeEnd()));
			}
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			DelinesBusField delinesBusField = fieldParser.parse(field);
			if (delinesBusField != null) {
				delinesBusFields.add(delinesBusField);
			}
		}
		return bus;
	}
}
