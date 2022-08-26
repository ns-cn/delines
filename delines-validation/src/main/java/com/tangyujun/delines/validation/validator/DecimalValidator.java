package com.tangyujun.delines.validation.validator;

import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;

import java.util.function.BiPredicate;
import java.util.function.Function;

public abstract class DecimalValidator<T> implements IInnerValidator<T> {

	/**
	 * 检验数据和约束的通用方法
	 *
	 * @param data      提供的数据
	 * @param value     约束的值
	 * @param message   提示信息
	 * @param parser    提供数据的转换方法
	 * @param predicate 比较方法
	 * @param <D>       数据类型
	 * @return 检验结果
	 */
	protected <D> ValidationResult validate(D data, String value, String message, Function<String, D> parser,
	                                        BiPredicate<D, D> predicate) {
		return predicate.test(data, parser.apply(value)) ? ValidationResult.ok() : ValidationResult.fail(message);
	}
}
