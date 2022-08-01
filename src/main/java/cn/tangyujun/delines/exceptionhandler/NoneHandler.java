package cn.tangyujun.delines.exceptionhandler;

import cn.tangyujun.delines.decoder.IDelinesDecoder;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

public class NoneHandler implements IDelinesDecoder.ExceptionHandler {
	@Override
	public void handle(Matcher matcher, Object entity, Field field, Throwable exception) {

	}
}
