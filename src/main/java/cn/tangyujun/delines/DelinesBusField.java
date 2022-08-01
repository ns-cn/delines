package cn.tangyujun.delines;

import cn.tangyujun.delines.decoder.IDelinesDecoder;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class DelinesBusField {

	private Field field;

	private Class<?> resultType;
	private String name;
	private IDelinesDecoder decoder;
	private IDelinesDecoder.ExceptionHandler decodeExceptionHandler;
	private String regExp;
	private Pattern pattern;

	public DelinesBusField(Field field) {
		this.field = field;
		this.field.setAccessible(true);
		this.resultType = field.getType();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
