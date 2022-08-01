package cn.tangyujun.delines;

import cn.tangyujun.delines.decoder.IDelinesDecoder;
import cn.tangyujun.delines.parser.DelinesEntityParser;

import java.util.List;
import java.util.regex.Matcher;

public final class Delines {

	public static <T extends IDelinesEntity> T with(String data, Class<T> clazz) {
		return with(data, DelinesEntityParser.parse(clazz));
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
		// 有行的强制正则校验，必须满足才能进行转换
		if (entity.getRequired() != null && !entity.getRequired().matcher(data).matches()) {
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
					Object fieldValue = decoder.decode(matcher, field.getResultType());
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
