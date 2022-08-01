package cn.tangyujun.delines.parser;

import cn.tangyujun.delines.DelinesBusEntity;
import cn.tangyujun.delines.DelinesBusField;
import cn.tangyujun.delines.IDelinesEntity;
import cn.tangyujun.delines.annotation.DelinesEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public final class DelinesEntityParser {
	public static <T extends IDelinesEntity> DelinesBusEntity<T> parse(Class<T> clazz) {
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
				bus.setRangeStartLine(Long.valueOf(delinesEntity.rangeStart()));
			} else if (DelinesEntity.RangeType.REGULAR.equals(rangeStartType)) {
				bus.setRangeStartPattern(Pattern.compile(delinesEntity.rangeStart()));
			}
			if (DelinesEntity.RangeType.NUMBER.equals(rangeEndType)) {
				bus.setRangeEndLine(Long.valueOf(delinesEntity.rangeEnd()));
			} else if (DelinesEntity.RangeType.REGULAR.equals(rangeEndType)) {
				bus.setRangeEndPattern(Pattern.compile(delinesEntity.rangeEnd()));
			}
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			DelinesBusField delinesBusField = DelinesFieldParser.parse(field);
			if (delinesBusField != null) {
				delinesBusFields.add(delinesBusField);
			}
		}
		return bus;
	}
}
