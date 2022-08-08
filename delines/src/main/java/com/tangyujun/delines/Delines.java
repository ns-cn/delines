package com.tangyujun.delines;

import com.tangyujun.delines.annotation.DelinesEntity;
import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.parser.DelinesEntityParser;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

public final class Delines {

	public static <T extends IDelinesEntity> T with(String data, Class<T> clazz) {
		return with(data, clazz, null);
	}

	public static <T extends IDelinesEntity> T with(String data, Class<T> clazz, DelinesEntityParser customParser) {
		DelinesEntityParser entityParser = Optional.ofNullable(customParser).orElse(DelinesEntityParser.DEFAULT);
		return with(data, entityParser.parse(clazz));
	}

	public static <T extends IDelinesEntity> T with(String data, DelinesBusEntity<T> entity) {
		return with(DelinesLine.of(data), entity);
	}

	public static <T extends IDelinesEntity> T with(DelinesLine line, DelinesBusEntity<T> entity) {
		if (line == null) {
			return null;
		}
		String data = line.getData();
		if (data == null || data.equals("") || entity == null) {
			return null;
		}
		if (entity.getRequired() != null && !entity.getRequired().matcher(data).matches()) {
			return null;
		}
		// 根据正则做限制的，如果成功匹配则更新开始和结束行的行号
		if (entity.getRangeStartLine() == null
				&& DelinesEntity.RangeType.REGULAR.equals(entity.getRangeStartType())
				&& entity.getRangeStartPattern().matcher(data).matches()) {
			if (DelinesEntity.RangeBorder.EXCLUDE.equals(entity.getRangeStartBorder())) {
				entity.setRangeStartLine(line.getLineIndex() + 1);
			} else {
				entity.setRangeStartLine(line.getLineIndex());
			}
		}
		if (entity.getRangeEndLine() == null
				&& DelinesEntity.RangeType.REGULAR.equals(entity.getRangeEndType())
				&& entity.getRangeEndPattern().matcher(data).matches()) {
			if (DelinesEntity.RangeBorder.EXCLUDE.equals(entity.getRangeEndBorder())) {
				entity.setRangeEndLine(line.getLineIndex() - 1);
			} else {
				entity.setRangeEndLine(line.getLineIndex());
			}
		}
		// 超出匹配范围，直接返回行号
		if ((entity.getRangeStartLine() != null && entity.getRangeStartLine() > line.getLineIndex())
				|| (entity.getRangeEndLine() != null && entity.getRangeEndLine() < line.getLineIndex())) {
			return null;
		}
		T t = entity.create();
		List<DelinesBusField> fields = entity.getFields();
		if (fields != null) {
			for (DelinesBusField field : fields) {
				Matcher matcher = field.getPattern().matcher(data);
				IDelinesDecoder decoder = field.getDecoder();
				IDelinesDecoder.ExceptionHandler exceptionHandler = field.getDecodeExceptionHandler();
				try {
					Object fieldValue = decoder.decode(matcher, field);
					field.getField().set(t, fieldValue);
				} catch (Exception e) {
					exceptionHandler.handle(matcher, t, field.getField(), e);
				}
			}
		}
		t.setLineIndex(line.getLineIndex());
		return t;
	}
}
