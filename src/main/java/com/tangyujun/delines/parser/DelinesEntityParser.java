package com.tangyujun.delines.parser;

import cn.hutool.core.text.CharSequenceUtil;
import com.tangyujun.delines.DelinesBusEntity;
import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.annotation.DelinesEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 实体映射类解析，含类注解和字段注解两种数据的解析
 */
public final class DelinesEntityParser {


	/**
	 * 实体映射类中字段的映射解析器
	 */
	private DelinesFieldParser fieldParser;

	/**
	 * 默认的构造器，使用默认的字段解析器解析
	 */
	private DelinesEntityParser() {
		fieldParser = DelinesFieldParser.DEFAULT;
	}

	/**
	 * 使用定制的字段解析器
	 *
	 * @param fieldParser 定制的字段解析器
	 */
	private DelinesEntityParser(DelinesFieldParser fieldParser) {
		this.setFieldParser(fieldParser);
	}


	/**
	 * 可定制的实体映射解析器
	 *
	 * @return 实体映射解析器
	 */
	public static DelinesEntityParser custom() {
		return new DelinesEntityParser();
	}

	/**
	 * 字段解析器
	 *
	 * @param fieldParser 自定制的字段解析器
	 */
	public void setFieldParser(DelinesFieldParser fieldParser) {
		Objects.requireNonNull(fieldParser, "could not build a DelinesEntityParser without DelinesFieldParser");
		this.fieldParser = fieldParser;
	}

	/**
	 * 自定义字段解析器
	 *
	 * @param fieldParser 自定义的字段解析器
	 * @return 自定义字段解析器的实体类型解析器
	 */
	public DelinesEntityParser withFieldParser(DelinesFieldParser fieldParser) {
		setFieldParser(fieldParser);
		return this;
	}

	/**
	 * 解析具体的实体映射类型
	 *
	 * @param clazz 实体映射类型
	 * @param <T>   类类型
	 * @return 映射转换之后的数据
	 */
	public <T> DelinesBusEntity<T> parse(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		List<DelinesBusField> delinesBusFields = new ArrayList<>();
		DelinesBusEntity<T> bus = DelinesBusEntity.of(clazz, delinesBusFields);
		// 类含有{@link DelinesEntity}注解，则处理对实体的描述内容
		DelinesEntity delinesEntity = clazz.getAnnotation(DelinesEntity.class);
		if (delinesEntity != null) {
			String required = delinesEntity.required();
			DelinesEntity.RangeType rangeStartType = delinesEntity.rangeStartType();
			DelinesEntity.RangeType rangeEndType = delinesEntity.rangeEndType();
			if (!CharSequenceUtil.isBlank(required)) {
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
		// 解析实体的字段，仅解析带{@link DelinesField}注解的字段
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			DelinesBusField delinesBusField = fieldParser.parse(field);
			if (delinesBusField != null) {
				delinesBusFields.add(delinesBusField);
			}
		}
		return bus;
	}


	/**
	 * 默认的解析器，使用默认的字段解析器解析
	 */
	public static final DelinesEntityParser DEFAULT = new DelinesEntityParser();

	/**
	 * 使用默认的实体解析器解析具体的映射类型
	 *
	 * @param clazz 实体映射类型
	 * @param <T>   类类型
	 * @return 映射转换之后的数据
	 */
	public static <T> DelinesBusEntity<T> parseUsingDefault(Class<T> clazz) {
		return DEFAULT.parse(clazz);
	}
}
