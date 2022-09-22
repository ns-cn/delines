package com.tangyujun.delines.validation;

// 校验类型
public enum ValidationType {
	// 成功
	OK,
	// 警告
	WARN,
	// 失败
	FAIL;

	// 校验所有内容
	public static final int OPTIONS_ALL = 0b111;
	// 仅校验失败的内容
	public static final int OPTIONS_FINAL = 0b100;

	/**
	 * 组合多种校验类型结果构建校验选项
	 *
	 * @param types 校验类型集合
	 * @return 校验选项
	 */
	public static int options(ValidationType... types) {
		int options = 0;
		for (ValidationType type : types) {
			if (type != null) {
				options = options | (1 << type.ordinal());
			}
		}
		return options;
	}

	/**
	 * 根据校验选项结果，判断是否需要校验当前类型
	 *
	 * @param options 校验选项
	 * @return 是否需要校验
	 */
	public boolean op(int options) {
		return (options & (1 << ordinal())) > 0;
	}
}
