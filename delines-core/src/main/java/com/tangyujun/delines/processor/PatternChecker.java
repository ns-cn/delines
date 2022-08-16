package com.tangyujun.delines.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.regex.Pattern;

/**
 * 正则的校验
 */
public interface PatternChecker {

	/**
	 * 实际校验，并对正则校验抛出的异常打印编译异常消息
	 */
	static boolean check(String pattern, Messager messager, Element element) {
		try {
			Pattern.compile(pattern);
			return true;
		} catch (Exception e) {
			messager.printMessage(Diagnostic.Kind.ERROR, "wrong Pattern: " + pattern, element);
			return false;
		}
	}
}
