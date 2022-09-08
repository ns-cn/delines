package com.tangyujun.delines.parser;

import cn.hutool.core.text.CharSequenceUtil;
import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.annotation.DelinesNestedField;
import com.tangyujun.delines.decoder.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class DelinesFieldParser {

	public static final DelinesFieldParser DEFAULT = new DelinesFieldParser();
	private IDecoderFactory decoderFactory = new DefaultDecoderFactory();
	private IDecoderExceptionHandlerFactory decodeExceptionHandlerFactory = new DefaultDecodeExceptionHandlerFactory();

	private DelinesFieldParser(IDecoderFactory decoderFactory,
	                           IDecoderExceptionHandlerFactory decodeExceptionHandlerFactory) {
		setDecoderFactory(decoderFactory);
		setDecodeExceptionHandlerFactory(decodeExceptionHandlerFactory);
	}

	private DelinesFieldParser() {
	}

	public static DelinesBusField parseUsingDefault(Field field) {
		return DEFAULT.parse(field);
	}

	public static DelinesFieldParser custom() {
		return new DelinesFieldParser();
	}

	public void setDecoderFactory(IDecoderFactory decoderFactory) {
		Objects.requireNonNull(decoderFactory, "errors: could not build a parser without decoder factory!");
		this.decoderFactory = decoderFactory;
	}

	public void setDecodeExceptionHandlerFactory(IDecoderExceptionHandlerFactory decodeExceptionHandlerFactory) {
		Objects.requireNonNull(decodeExceptionHandlerFactory, "errors: could not build a parser without decoder " +
				"exception handler factory!");
		this.decodeExceptionHandlerFactory = decodeExceptionHandlerFactory;
	}

	/**
	 * 解析实体映射类型字段
	 *
	 * @param field 字段
	 * @return 解析之后实体映射类型
	 */
	public DelinesBusField parse(Field field) {
		if (field == null) {
			return null;
		}
		DelinesField delinesField = field.getAnnotation(DelinesField.class);
		DelinesNestedField delinesNestedField = field.getAnnotation(DelinesNestedField.class);
		if (delinesField != null && delinesNestedField != null) {
			throw new RuntimeException("could not @DelinesField when @DelinesNestedField");
		}
		if (delinesField == null && delinesNestedField == null) {
			return null;
		}
		DelinesBusField delinesBusField = new DelinesBusField(field);
		Optional.ofNullable(delinesField).ifPresent(t -> {
			Class<? extends IDelinesDecoder> clazzDecoder = t.decoder();
			Class<? extends IDelinesDecoder.ExceptionHandler> clazzDecodeExceptionHandler = t.decodeExceptionHandler();
			String regExp = t.value();
			IDelinesDecoder decoder = decoderFactory.get(clazzDecoder);
			IDelinesDecoder.ExceptionHandler exceptionHandler = decodeExceptionHandlerFactory.get(clazzDecodeExceptionHandler);
			delinesBusField.setDecoder(decoder);
			delinesBusField.setDecodeExceptionHandler(exceptionHandler);
			delinesBusField.setRegExp(regExp);
			delinesBusField.setDateFormat(t.dateFormat());
		});
		Optional.ofNullable(delinesNestedField).ifPresent(t -> {
			delinesBusField.setNestedField(true);
			if (CharSequenceUtil.isNotBlank(t.value())) {
				delinesBusField.setRegExp(t.value());
			}
			if (List.class.equals(delinesBusField.getResultType()) || Set.class.equals(delinesBusField.getResultType())) {
				Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
				if (actualTypeArguments.length != 1) {
					throw new RuntimeException("type assignment required!");
				}
				try {
					Class<?> innerType = Class.forName(actualTypeArguments[0].getTypeName());
					delinesBusField.setNestedBusEntity(DelinesEntityParser.parseUsingDefault(innerType));
					delinesBusField.setNestedSubType(innerType);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			} else {
				delinesBusField.setNestedBusEntity(DelinesEntityParser.parseUsingDefault(delinesBusField.getResultType()));
			}
		});
		return delinesBusField;
	}

	public DelinesFieldParser withDecoderFactory(IDecoderFactory decoderFactory) {
		this.setDecoderFactory(decoderFactory);
		return this;
	}

	public DelinesFieldParser withDecodeExceptionHandlerFactory(IDecoderExceptionHandlerFactory decodeExceptionHandlerFactory) {
		this.setDecodeExceptionHandlerFactory(decodeExceptionHandlerFactory);
		return this;
	}
}
