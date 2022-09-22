package com.tangyujun.delines;

import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.decoder.IEntityFactory;
import com.tangyujun.delines.parser.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析后的字段
 */
public class DelinesBusField {

	/**
	 * 是否是可嵌套的字段类型
	 */
	private boolean nestedField = false;

	/**
	 * 反射内容字段
	 */
	private Field field;

	/**
	 * 字段的实际类型
	 */
	private Class<?> resultType;

	/**
	 * 字段的解析器
	 */
	private IDelinesDecoder decoder;
	/**
	 * 字段的解析异常处理器
	 */
	private IDelinesDecoder.ExceptionHandler decodeExceptionHandler;
	/**
	 * 字段的原始正则表达式
	 * <p>如果是嵌套类型，则里面的字符串读取的方式</p>
	 * 子字段读取字符串的方式，通过父字段的匹配字符串读取还是从原始字符串读取
	 * <p>默认为通过父字段匹配的字符串读取</p>
	 */
	private String regExp;
	/**
	 * 字段的原始正则表达式编译后的pattern
	 * <p>如果是嵌套类型，则里面的字符串读取的方式</p>
	 * 子字段读取字符串的方式，通过父字段的匹配字符串读取还是从原始字符串读取
	 * <p>默认为通过父字段匹配的字符串读取</p>
	 */
	private Pattern pattern;
	/**
	 * 如果是日期类型时，日期类型的格式
	 */
	private String dateFormat;

	/**
	 * 如果是嵌套子类型，记录子所有子字段
	 */
	private DelinesBusEntity<?> nestedBusEntity;

	/**
	 * 泛型子类型
	 */
	private Class<?> nestedSubType;

	/**
	 * 解析后的字段
	 *
	 * @param field 原始字段
	 */
	public DelinesBusField(Field field) {
		this.field = field;
		this.field.setAccessible(true);
		this.resultType = field.getType();
	}

	/**
	 * 是否是嵌套字段
	 *
	 * @return 是否是嵌套字段
	 */
	public boolean isNestedField() {
		return nestedField;
	}

	/**
	 * 设置本字段是否是嵌套字段
	 *
	 * @param nestedField 是否是嵌套字段
	 */
	public void setNestedField(boolean nestedField) {
		this.nestedField = nestedField;
	}

	/**
	 * 获取原始子弹
	 *
	 * @return 原始字段
	 */
	public Field getField() {
		return field;
	}

	/**
	 * 设置原始字段
	 *
	 * @param field 原始字段
	 */
	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * 获取字段的类型
	 *
	 * @return 字段类型
	 */
	public Class<?> getResultType() {
		return resultType;
	}

	/**
	 * 设置字段的类型
	 *
	 * @param resultType 字段类型
	 */
	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}

	/**
	 * 获取字段的解析器
	 *
	 * @return 字段的解析器
	 */
	public IDelinesDecoder getDecoder() {
		return decoder;
	}

	/**
	 * 设置字段的解析器
	 *
	 * @param decoder 字段的解析器
	 */
	public void setDecoder(IDelinesDecoder decoder) {
		this.decoder = decoder;
	}

	/**
	 * 字段的解析异常处理器
	 *
	 * @return 字段的解析异常处理器
	 */
	public IDelinesDecoder.ExceptionHandler getDecodeExceptionHandler() {
		return decodeExceptionHandler;
	}

	/**
	 * 字段的解析异常处理器
	 *
	 * @param decodeExceptionHandler 字段的解析异常处理器
	 */
	public void setDecodeExceptionHandler(IDelinesDecoder.ExceptionHandler decodeExceptionHandler) {
		this.decodeExceptionHandler = decodeExceptionHandler;
	}

	/**
	 * 获取字段的原始匹配正则
	 *
	 * @return 字段的原始匹配正则
	 */
	public String getRegExp() {
		return regExp;
	}

	/**
	 * 设置字段的原始匹配正则
	 *
	 * @param regExp 字段的原始匹配正则
	 */
	public void setRegExp(String regExp) {
		this.regExp = regExp;
		pattern = Pattern.compile(regExp);
	}

	/**
	 * 获取字段编译后的pattern
	 *
	 * @return 字段编译后的pattern
	 */
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * 设置字段编译后的pattern
	 *
	 * @return 字段编译后的pattern
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * 设置时间类型的时间格式
	 *
	 * @param dateFormat 时间格式
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * 获取嵌套类型的解析后的实体
	 *
	 * @return 嵌套类型解析后的实体
	 */
	public DelinesBusEntity<?> getNestedBusEntity() {
		return nestedBusEntity;
	}

	/**
	 * 设置嵌套类型的解析后的实体
	 *
	 * @param nestedBusEntity 嵌套类型解析后的实体
	 */
	public void setNestedBusEntity(DelinesBusEntity<?> nestedBusEntity) {
		this.nestedBusEntity = nestedBusEntity;
	}

	/**
	 * 嵌套类型的具体类型
	 *
	 * @return 嵌套类型的具体类型
	 */
	public Class<?> getNestedSubType() {
		return nestedSubType;
	}

	/**
	 * 设置嵌套类型的具体类型
	 *
	 * @param nestedSubType 设置嵌套类型的具体类型
	 */
	public void setNestedSubType(Class<?> nestedSubType) {
		this.nestedSubType = nestedSubType;
	}

	/**
	 * 通过给定字符串数据构建指定对象的本字段数据
	 *
	 * @param object 指定目标对象
	 * @param data   给定字符串数据
	 */
	public void build(Object object, String data) {
		Object fieldValue = null;
		if (nestedField) {
			if (nestedBusEntity != null) {
				if (List.class.equals(resultType) || Set.class.equals(resultType)) {
					Collection<Object> tempFieldValue = null;
					if (List.class.equals(resultType)) {
						tempFieldValue = CollectionUtils.newList(Object.class);
					} else {
						tempFieldValue = CollectionUtils.newSet(Object.class);
					}
					if (pattern != null) {
						Matcher matcher = pattern.matcher(data);
						while (matcher.find()) {
							String innerDate = matcher.group();
							Object tempItem = IEntityFactory.build(nestedSubType);
							Collection<Object> finalTempFieldValue = tempFieldValue;
							Optional.ofNullable(nestedBusEntity)
									.map(DelinesBusEntity::getFields)
									.ifPresent(fs -> {
										fs.forEach(f -> f.build(tempItem, innerDate));
										finalTempFieldValue.add(tempItem);
									});
						}
					} else {
						Object tempItem = IEntityFactory.build(nestedSubType);
						Collection<Object> finalTempFieldValue = tempFieldValue;
						Optional.ofNullable(nestedBusEntity)
								.map(DelinesBusEntity::getFields)
								.ifPresent(fs -> {
									fs.forEach(f -> f.build(tempItem, data));
									finalTempFieldValue.add(tempItem);
								});
					}
					fieldValue = tempFieldValue;
				} else {
					fieldValue = IEntityFactory.build(resultType);
					String subMatchData = Optional.ofNullable(pattern)
							.map(t -> t.matcher(data))
							.map(t -> t.find() ? t : null)
							.map(Matcher::group)
							.orElse(data);
					Object finalFieldValue = fieldValue;
					Optional.ofNullable(nestedBusEntity)
							.map(DelinesBusEntity::getFields)
							.ifPresent(fs -> fs.forEach(f -> f.build(finalFieldValue, subMatchData)));
				}
				try {
					field.set(object, fieldValue);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			Matcher matcher = pattern.matcher(data);
			try {
				fieldValue = decoder.decode(matcher, this);
				field.set(object, fieldValue);
			} catch (Exception e) {
				decodeExceptionHandler.handle(matcher, object, field, e);
			}
		}
	}
}
