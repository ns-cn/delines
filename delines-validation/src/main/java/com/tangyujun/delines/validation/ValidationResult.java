package com.tangyujun.delines.validation;

import cn.hutool.core.collection.CollectionUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ValidationResult {

	private ValidationType status;

	private String message;

	private ValidationResult(ValidationType status) {
		this.status = status;
	}

	public ValidationResult(ValidationType status, String message) {
		Objects.requireNonNull(status);
		this.status = status;
		this.message = message;
	}

	public static ValidationResult ok() {
		return ok(null);
	}

	public static ValidationResult ok(String message) {
		return new ValidationResult(ValidationType.OK, message);
	}

	public static ValidationResult warn() {
		return warn(null);
	}

	public static ValidationResult warn(String message) {
		return new ValidationResult(ValidationType.WARN, message);
	}

	public static ValidationResult fail() {
		return fail(null);
	}

	public static ValidationResult fail(String message) {
		return new ValidationResult(ValidationType.FAIL, message);
	}

	public ValidationType getStatus() {
		return Optional.ofNullable(status).orElse(ValidationType.OK);
	}

	public String getWarnMessage() {
		return ValidationType.WARN.equals(status) ? message : null;
	}

	public String getFailMessage() {
		return ValidationType.FAIL.equals(status) ? message : null;
	}

	public void check() throws ValidationException {
		ValidationException exception = Optional.ofNullable(getFailMessage())
				.map(ValidationException::new)
				.orElse(null);
		if (exception != null) {
			throw exception;
		}
	}

	public String getMessage() {
		return message;
	}


	public boolean isOk() {
		return ValidationType.OK.equals(status);
	}

	public boolean isWarn() {
		return ValidationType.WARN.equals(status);
	}

	public boolean isFail() {
		return ValidationType.FAIL.equals(status);
	}

	/**
	 * 合并所有校验结果
	 *
	 * @param results 校验结果集合
	 * @return 合并之后的校验结果
	 */
	public static ValidationResult merge(ValidationResult... results) {
		return merge(ValidationType.OPTIONS_ALL, results);
	}

	/**
	 * 合并所有校验结果，可根据校验选项配置合并的校验结果类型
	 *
	 * @param options 校验选项
	 * @param results 校验结果集合
	 * @return 合并之后的校验结果
	 */
	public static ValidationResult merge(int options, ValidationResult... results) {
		if (results.length == 0) {
			return ValidationResult.ok();
		}
		List<String> okMessage = new LinkedList<>();
		List<String> warnMessage = new LinkedList<>();
		List<String> failMessage = new LinkedList<>();
		ValidationType finalType = ValidationType.OK;
		EnumMap<ValidationType, Consumer<String>> messageHandlers = new EnumMap<>(ValidationType.class);
		EnumMap<ValidationType, Function<String, ValidationResult>> resultBuilder = new EnumMap<>(ValidationType.class);
		messageHandlers.put(ValidationType.OK, okMessage::add);
		messageHandlers.put(ValidationType.WARN, warnMessage::add);
		messageHandlers.put(ValidationType.FAIL, failMessage::add);
		resultBuilder.put(ValidationType.OK, ValidationResult::ok);
		resultBuilder.put(ValidationType.WARN, ValidationResult::warn);
		resultBuilder.put(ValidationType.FAIL, ValidationResult::fail);
		for (ValidationResult result : results) {
			if (result != null && result.getStatus().op(options)) {
				Optional.ofNullable(result.getMessage()).ifPresent(messageHandlers.get(result.getStatus()));
				if (result.getStatus().ordinal() > finalType.ordinal()) {
					finalType = result.getStatus();
				}
			}
		}
		StringBuilder resultMessage = new StringBuilder();
		Map<ValidationType, List<String>> resultMessageMapper = new EnumMap<>(ValidationType.class);
		resultMessageMapper.put(ValidationType.OK, okMessage);
		resultMessageMapper.put(ValidationType.WARN, warnMessage);
		resultMessageMapper.put(ValidationType.FAIL, failMessage);
		for (ValidationType type : ValidationType.values()) {
			if (type.op(options)) {
				Optional.of(resultMessageMapper.get(type))
						.map(t -> CollectionUtil.isEmpty(t) ? null : String.join(",", t) + ";")
						.ifPresent(resultMessage::append);
			}
		}
		return resultBuilder.get(finalType).apply(resultMessage.toString());
	}
}
