package cn.tangyujun.delines.parser;

import cn.tangyujun.delines.DelinesBusField;
import cn.tangyujun.delines.decoder.IDelinesDecoder;
import cn.tangyujun.delines.annotation.DelinesField;
import cn.tangyujun.delines.decoder.DecodeExceptionHandlerFactory;
import cn.tangyujun.delines.decoder.DecoderFactory;

import java.lang.reflect.Field;

public final class DelinesFieldParser {

	public static DelinesBusField parse(Field field) {
		if (field == null) {
			return null;
		}
		DelinesField delinesField = field.getAnnotation(DelinesField.class);
		if( delinesField==null){
			return null;
		}
		String name = delinesField.name();
		Class<? extends IDelinesDecoder> clazzDecoder = delinesField.decoder();
		Class<? extends IDelinesDecoder.ExceptionHandler> clazzDecodeExceptionHandler =
				delinesField.decodeExceptionHandler();
		String regExp = delinesField.regExp();
		IDelinesDecoder decoder = DecoderFactory.get(clazzDecoder);
		IDelinesDecoder.ExceptionHandler exceptionHandler = DecodeExceptionHandlerFactory.get(clazzDecodeExceptionHandler);
		DelinesBusField delinesBusField = new DelinesBusField(field);
		delinesBusField.setName(name);
		delinesBusField.setDecoder(decoder);
		delinesBusField.setDecodeExceptionHandler(exceptionHandler);
		delinesBusField.setRegExp(regExp);
		return delinesBusField;
	}
}
