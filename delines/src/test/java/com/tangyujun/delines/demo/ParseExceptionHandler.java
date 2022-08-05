package com.tangyujun.delines.demo;

import com.tangyujun.delines.decoder.IDelinesDecoder;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

public class ParseExceptionHandler implements IDelinesDecoder.ExceptionHandler {
	@Override
	public void handle(Matcher matcher, Object entity, Field field, Throwable exception) {
		exception.printStackTrace();
	}
}
