package com.tangyujun.delines.handler;

import com.tangyujun.delines.decoder.IDelinesDecoder;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

/**
 * 解析异常不处理
 */
public class NoneHandler implements IDelinesDecoder.ExceptionHandler {
	@Override
	public void handle(Matcher matcher, Object entity, Field field, Throwable exception) {

	}
}
