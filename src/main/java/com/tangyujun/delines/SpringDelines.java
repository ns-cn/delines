package com.tangyujun.delines;

import com.tangyujun.delines.parser.DelinesEntityParser;
import com.tangyujun.delines.parser.DelinesFieldParser;

public class SpringDelines {

	public static final DelinesEntityParser DELINES_ENTITY_SPRING_PARSER = DelinesEntityParser.custom()
			.withFieldParser(DelinesFieldParser
					.custom()
					.withDecoderFactory(new DecoderSpringFactory())
					.withDecodeExceptionHandlerFactory(new DecodeExceptionHandlerSpringFactory()));


	public static <T> T with(String data, Class<T> clazz) {
		return Delines.with(data, clazz, DELINES_ENTITY_SPRING_PARSER);
	}

	public static <T> T with(String data, DelinesBusEntity<T> entity) {
		return Delines.with(data, entity);
	}

	public static <T extends IDelinesEntity> T with(DelinesLine line, DelinesBusEntity<T> entity) {
		return Delines.with(line, entity);
	}
}
