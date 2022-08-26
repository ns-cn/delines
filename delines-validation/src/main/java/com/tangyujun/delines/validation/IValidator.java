package com.tangyujun.delines.validation;

/**
 * 自定义校验内容
 */
public interface IValidator {

	/**
	 * 校验操作
	 *
	 * @param data 字段数据
	 * @return 校验结果
	 */
	ValidationResult validate(Object data);
}
