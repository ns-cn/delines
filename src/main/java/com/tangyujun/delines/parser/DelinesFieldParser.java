package com.tangyujun.delines.parser;

import cn.hutool.core.text.CharSequenceUtil;
import com.tangyujun.delines.spring.SpringDecodeExceptionHandlerFactory;
import com.tangyujun.delines.spring.SpringDecoderFactory;
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

/**
 * 字段解析器
 */
public final class DelinesFieldParser {

	/**
	 * 默认字段解析器
	 */
	public static final DelinesFieldParser DEFAULT = new DelinesFieldParser();
	/**
	 * 默认解析器工厂
	 */
	private IDecoderFactory decoderFactory = new DefaultDecoderFactory();
	/**
	 * 解析异常处理器工厂
	 */
	private IDecoderExceptionHandlerFactory decodeExceptionHandlerFactory = new DefaultDecodeExceptionHandlerFactory();

	/**
	 * DelinesFieldParser
	 *
	 * @param decoderFactory                解析器工厂
	 * @param decodeExceptionHandlerFactory 解析异常处理器工厂
	 */
	private DelinesFieldParser(IDecoderFactory decoderFactory,
	                           IDecoderExceptionHandlerFactory decodeExceptionHandlerFactory) {
		setDecoderFactory(decoderFactory);
		setDecodeExceptionHandlerFactory(decodeExceptionHandlerFactory);
	}

	/**
	 * DelinesFieldParser
	 */
	private DelinesFieldParser() {
	}

	/**
	 * 用默认的解析器解析
	 *
	 * @param field 字段
	 * @return 解析后的bus对象
	 */
	public static DelinesBusField parseUsingDefault(Field field) {
		return DEFAULT.parse(field);
	}

	/**
	 * 定制化的解析器
	 *
	 * @return 解析器
	 */
	public static DelinesFieldParser custom() {
		return new DelinesFieldParser();
	}

	/**
	 * 设置解析器工厂
	 *
	 * @param decoderFactory 解析器工厂
	 */
	public void setDecoderFactory(IDecoderFactory decoderFactory) {
		Objects.requireNonNull(decoderFactory, "errors: could not build a parser without decoder factory!");
		this.decoderFactory = decoderFactory;
	}

	/**
	 * 设置解析异常处理器工厂
	 *
	 * @param decodeExceptionHandlerFactory 解析异常处理器工厂
	 */
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

	/**
	 * 设置解析器工厂
	 *
	 * @param decoderFactory 解析器工厂
	 * @return DelinesFieldParser
	 */
	public DelinesFieldParser withDecoderFactory(SpringDecoderFactory decoderFactory) {
		this.setDecoderFactory(decoderFactory);
		return this;
	}

	/**
	 * 设置解析异常处理器工厂
	 *
	 * @param decodeExceptionHandlerFactory 解析异常处理器工厂
	 * @return DelinesFieldParser
	 */
	public DelinesFieldParser withDecodeExceptionHandlerFactory(SpringDecodeExceptionHandlerFactory decodeExceptionHandlerFactory) {
		this.setDecodeExceptionHandlerFactory(decodeExceptionHandlerFactory);
		return this;
	}
}
