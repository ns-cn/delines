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
	 *
	 * @param pattern  正则格式
	 * @param messager messager
	 * @param element  element
	 * @return 校验正则格式是否符合正则格式要求
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

	/**
	 * 校验整数类型
	 *
	 * @param number   整数字符串
	 * @param messager messager
	 * @param element  element
	 * @return 是否校验成功
	 */
	static boolean checkInteger(String number, Messager messager, Element element) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (Exception e) {
			messager.printMessage(Diagnostic.Kind.ERROR, "wrong Integer: " + number, element);
			return false;
		}
	}
}
