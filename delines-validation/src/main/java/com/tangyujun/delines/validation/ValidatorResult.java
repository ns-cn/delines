package com.tangyujun.delines.validation;

public class ValidatorResult {

	private Type status;

	private String message;

	private ValidatorResult(Type status) {
		this.status = status;
	}

	private ValidatorResult(Type status, String message) {
		this.status = status;
		this.message = message;
	}

	public static ValidatorResult ok() {
		return ok(null);
	}

	public static ValidatorResult ok(String message) {
		return new ValidatorResult(Type.OK, message);
	}

	public static ValidatorResult warn() {
		return warn(null);
	}

	public static ValidatorResult warn(String message) {
		return new ValidatorResult(Type.WARN, message);
	}

	public static ValidatorResult fail() {
		return fail(null);
	}

	public static ValidatorResult fail(String message) {
		return new ValidatorResult(Type.FAIL, message);
	}

	public enum Type {
		OK, WARN, FAIL;
	}

	public Type getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
