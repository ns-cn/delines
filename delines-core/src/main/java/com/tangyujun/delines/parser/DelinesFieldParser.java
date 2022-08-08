package com.tangyujun.delines.parser;

import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.decoder.*;

import java.lang.reflect.Field;
import java.util.Objects;

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

	public void setDecoderFactory(IDecoderFactory decoderFactory) {
		Objects.requireNonNull(decoderFactory, "errors: could not build a parser without decoder factory!");
		this.decoderFactory = decoderFactory;
	}

	public void setDecodeExceptionHandlerFactory(IDecoderExceptionHandlerFactory decodeExceptionHandlerFactory) {
		Objects.requireNonNull(decodeExceptionHandlerFactory, "errors: could not build a parser without decoder " +
				"exception handler factory!");
		this.decodeExceptionHandlerFactory = decodeExceptionHandlerFactory;
	}

	public DelinesBusField parse(Field field) {
		if (field == null) {
			return null;
		}
		DelinesField delinesField = field.getAnnotation(DelinesField.class);
		if (delinesField == null) {
			return null;
		}
		String name = delinesField.name();
		Class<? extends IDelinesDecoder> clazzDecoder = delinesField.decoder();
		Class<? extends IDelinesDecoder.ExceptionHandler> clazzDecodeExceptionHandler = delinesField.decodeExceptionHandler();
		String regExp = delinesField.regExp();
		IDelinesDecoder decoder = decoderFactory.get(clazzDecoder);
		IDelinesDecoder.ExceptionHandler exceptionHandler = decodeExceptionHandlerFactory.get(clazzDecodeExceptionHandler);
		DelinesBusField delinesBusField = new DelinesBusField(field);
		delinesBusField.setName(name);
		delinesBusField.setDecoder(decoder);
		delinesBusField.setDecodeExceptionHandler(exceptionHandler);
		delinesBusField.setRegExp(regExp);
		delinesBusField.setDateFormat(delinesField.dateFormat());
		return delinesBusField;
	}

	public static DelinesFieldParser custom() {
		return new DelinesFieldParser();
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
