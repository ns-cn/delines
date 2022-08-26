package com.tangyujun.delines.validation;

public interface IInnerValidator<T> extends IValidator{

	/**
	 * 校验操作
	 *
	 * @param data 字段数据
	 * @return 校验结果
	 */
	ValidationResult validate(Object data, T required);
}
