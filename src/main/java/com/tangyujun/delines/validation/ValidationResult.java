package com.tangyujun.delines.validation;

import cn.hutool.core.collection.CollectionUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 校验结果
 */
public class ValidationResult {

	/**
	 * 校验结果的类型
	 */
	private final ValidationType status;

	/**
	 * 提示信息
	 */
	private String message;

	/**
	 * 构造器
	 *
	 * @param status 校验结果类型
	 */
	private ValidationResult(ValidationType status) {
		this.status = status;
	}

	/**
	 * 构造器
	 *
	 * @param status  校验结果类型
	 * @param message 提示信息
	 */
	public ValidationResult(ValidationType status, String message) {
		Objects.requireNonNull(status);
		this.status = status;
		this.message = message;
	}

	/**
	 * 校验正常
	 *
	 * @return 校验结果
	 */
	public static ValidationResult ok() {
		return ok(null);
	}

	/**
	 * 校验正常
	 *
	 * @param message 提示信息
	 * @return 校验结果
	 */
	public static ValidationResult ok(String message) {
		return new ValidationResult(ValidationType.OK, message);
	}

	/**
	 * 存在警告信息
	 *
	 * @return 校验结果
	 */
	public static ValidationResult warn() {
		return warn(null);
	}

	/**
	 * 存在警告信息
	 *
	 * @param message 提示信息
	 * @return 校验结果
	 */
	public static ValidationResult warn(String message) {
		return new ValidationResult(ValidationType.WARN, message);
	}

	/**
	 * 校验失败
	 *
	 * @return 校验结果
	 */
	public static ValidationResult fail() {
		return fail(null);
	}

	/**
	 * 校验失败
	 *
	 * @param message 提示信息
	 * @return 校验结果
	 */
	public static ValidationResult fail(String message) {
		return new ValidationResult(ValidationType.FAIL, message);
	}

	/**
	 * 获取校验结果类型
	 *
	 * @return 校验结果类型
	 */
	public ValidationType getStatus() {
		return Optional.ofNullable(status).orElse(ValidationType.OK);
	}

	/**
	 * 如果是警告类型,则返回提示信息
	 *
	 * @return 警告提示信息
	 */
	public String getWarnMessage() {
		return ValidationType.WARN.equals(status) ? message : null;
	}

	/**
	 * 如果是失败类型,则返回提示信息
	 *
	 * @return 失败提示信息
	 */
	public String getFailMessage() {
		return ValidationType.FAIL.equals(status) ? message : null;
	}

	/**
	 * 校验,如果失败则抛出校验异常
	 *
	 * @throws ValidationException 校验异常
	 */
	public void check() throws ValidationException {
		ValidationException exception = Optional.ofNullable(getFailMessage())
				.map(ValidationException::new)
				.orElse(null);
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * 获取提示信息
	 * @return 提示信息
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 是否校验正常
	 * @return 是否正常
	 */
	public boolean isOk() {
		return ValidationType.OK.equals(status);
	}

	/**
	 * 是否是警告
	 * @return 是否警告
	 */
	public boolean isWarn() {
		return ValidationType.WARN.equals(status);
	}

	/**
	 * 是否失败
	 * @return 是否失败
	 */
	public boolean isFail() {
		return ValidationType.FAIL.equals(status);
	}

	/**
	 * 合并所有校验结果
	 *
	 * @param results 校验结果集合
	 * @return 合并之后的校验结果
	 */
	public static ValidationResult merge(Collection<ValidationResult> results) {
		return merge(ValidationType.OPTIONS_ALL, results);
	}

	/**
	 * 合并所有校验结果
	 *
	 * @param results 校验结果集合
	 * @return 合并之后的校验结果
	 */
	public static ValidationResult merge(ValidationResult... results) {
		return merge(ValidationType.OPTIONS_ALL, Arrays.asList(results));
	}

	/**
	 * 合并所有校验结果，可根据校验选项配置合并的校验结果类型
	 *
	 * @param options 校验选项
	 * @param results 校验结果集合
	 * @return 合并之后的校验结果
	 */
	public static ValidationResult merge(int options, Collection<ValidationResult> results) {
		if (CollectionUtil.isEmpty(results)) {
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

	@Override
	public String toString() {
		return String.format("[%s] - %s", status.name(),
				Optional.ofNullable(message).orElse(""));
	}
}
