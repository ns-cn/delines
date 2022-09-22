package com.tangyujun.delines.validation;

/**
 * 校验异常
 */
public class ValidationException extends Throwable {

	/**
	 * 校验异常
	 *
	 * @param message 异常内容
	 */
	public ValidationException(String message) {
		super(message);
	}
}
