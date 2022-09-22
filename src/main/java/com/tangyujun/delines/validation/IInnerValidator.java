package com.tangyujun.delines.validation;

/**
 * 内部的校验器
 *
 * @param <T> 校验类型
 */
public interface IInnerValidator<T> extends IValidator {

	/**
	 * 校验操作
	 *
	 * @param data     字段数据
	 * @param required 要求
	 * @return 校验结果
	 */
	ValidationResult validate(Object data, T required);
}
