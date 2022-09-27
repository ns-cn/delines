package com.tangyujun.delines;

import com.tangyujun.delines.annotation.DelinesEntity;
import com.tangyujun.delines.parser.DelinesEntityParser;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 单行实体解析器
 */
public final class Delines {

	/**
	 * 转换具体字符串为对应的实体<br>
	 * <p>核心流程：</p>
	 * 1. 使用实体解析器解析对应的类，得到实体装载器<br>
	 * 2. 使用装载器解析对应的字符串获得实体<br>
	 *
	 * @param data  字符串数据
	 * @param clazz 对应的具体数据
	 * @param <T>   类型泛型
	 * @return 解析后的数据
	 */
	public static <T> T with(String data, Class<T> clazz) {
		return with(data, clazz, null);
	}

	/**
	 * 转换具体字符串为对应的类型(待实体转换器)
	 * <p>核心流程：</p>
	 * 1. 使用实体解析器解析对应的类，得到实体装载器<br>
	 * 2. 使用装载器解析对应的字符串获得实体<br>
	 *
	 * @param data         字符串数据
	 * @param clazz        对应的具体类型
	 * @param customParser 定制化的转换器
	 * @param <T>          泛型类型
	 * @return 解析后的数据
	 */
	public static <T> T with(String data, Class<T> clazz, DelinesEntityParser customParser) {
		DelinesEntityParser entityParser = Optional.ofNullable(customParser).orElse(DelinesEntityParser.DEFAULT);
		return with(data, entityParser.parse(clazz));
	}

	/**
	 * 用转换后的实体和字符串数据转换数据
	 * <p>核心流程：</p>
	 * 1. 使用实体解析器解析对应的类，得到实体装载器<br>
	 * 2. 使用装载器解析对应的字符串获得实体<br>
	 *
	 * @param data   字符串数据
	 * @param entity 转换后的实体
	 * @param <T>    泛型类型
	 * @return 解析后的数据
	 */
	public static <T> T with(String data, DelinesBusEntity<T> entity) {
		Objects.requireNonNull(entity);
		if (data == null || data.isEmpty()) {
			return null;
		}
		if (entity.getRequired() != null && !entity.getRequired().matcher(data).matches()) {
			return null;
		}
		T t = entity.create();
		List<DelinesBusField> fields = entity.getFields();
		Optional.ofNullable(fields).ifPresent(fs -> fs.forEach(f -> f.build(t, data)));
		return t;
	}

	/**
	 * 解析带行数的字符串数据
	 *
	 * @param line   字符串数据
	 * @param entity 转换后的实体
	 * @param <T>    类型泛型
	 * @return 解析后的数据
	 */
	public static <T> T with(DelinesLine line, DelinesBusEntity<T> entity) {
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
		T t = with(data, entity);
		return t;
	}
}
