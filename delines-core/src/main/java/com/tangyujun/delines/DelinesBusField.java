package com.tangyujun.delines;

import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.decoder.IEntityFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private List<DelinesBusField> subFields;

	public DelinesBusField(Field field) {
		this.field = field;
		this.field.setAccessible(true);
		this.resultType = field.getType();
	}

	public boolean isNestedField() {
		return nestedField;
	}

	public void setNestedField(boolean nestedField) {
		this.nestedField = nestedField;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Class<?> getResultType() {
		return resultType;
	}

	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}

	public IDelinesDecoder getDecoder() {
		return decoder;
	}

	public void setDecoder(IDelinesDecoder decoder) {
		this.decoder = decoder;
	}

	public IDelinesDecoder.ExceptionHandler getDecodeExceptionHandler() {
		return decodeExceptionHandler;
	}

	public void setDecodeExceptionHandler(IDelinesDecoder.ExceptionHandler decodeExceptionHandler) {
		this.decodeExceptionHandler = decodeExceptionHandler;
	}

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
		pattern = Pattern.compile(regExp);
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void addSubFields(DelinesBusField field) {
		if (subFields == null) {
			subFields = new ArrayList<>();
		}
		subFields.add(field);
	}

	public List<DelinesBusField> getSubFields() {
		return subFields;
	}

	/**
	 * 通过给定字符串数据构建指定对象的本字段数据
	 *
	 * @param object 指定目标对象
	 * @param data   给定字符串数据
	 */
	public void build(Object object, String data) {
		Object fieldValue;
		if (nestedField) {
			fieldValue = IEntityFactory.build(resultType);
			String subMatchData = Optional.ofNullable(pattern)
					.map(t -> t.matcher(data))
					.map(Matcher::group)
					.orElse(data);
			if (subFields != null) {
				for (DelinesBusField subField : subFields) {
					subField.build(fieldValue, subMatchData);
				}
			}
			try {
				field.set(object, fieldValue);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
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
