package com.tangyujun.delines.validation;

import com.tangyujun.delines.decoder.IEntityFactory;
import com.tangyujun.delines.validation.annotation.*;
import com.tangyujun.delines.validation.factories.DefaultValidatorFactory;
import com.tangyujun.delines.validation.factories.SpringValidatorFactory;
import com.tangyujun.delines.validation.validator.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 校验操作
 */
public final class Validation {

	private IEntityFactory<IValidator> entityFactories = new DefaultValidatorFactory();

	public static final Validation DEFAULT = new Validation();

	private static final Map<Class<?>, Class<? extends IValidator>> HANDLED = new HashMap<>();

	static {
		HANDLED.put(AssertTrue.class, BooleanAssertTrueValidator.class);
		HANDLED.put(AssertFalse.class, BooleanAssertFalseValidator.class);
		HANDLED.put(AssertNotNull.class, ObjectNotNullValidator.class);
		HANDLED.put(AssertNull.class, ObjectNullValidator.class);
		HANDLED.put(DateFuture.class, DateFutureValidator.class);
		HANDLED.put(DatePast.class, DatePastValidator.class);
		HANDLED.put(DecimalMax.class, DecimalMaxValidator.class);
		HANDLED.put(DecimalMin.class, DecimalMinValidator.class);
		HANDLED.put(DecimalRange.class, DecimalRangeValidator.class);
		HANDLED.put(StringLength.class, StringLengthValidator.class);
		HANDLED.put(StringNotEmpty.class, StringNotEmptyValidator.class);
		HANDLED.put(StringPattern.class, StringPatternValidator.class);
	}

	private Validation() {
	}

	private Validation(IEntityFactory<IValidator> entityFactories) {
		Objects.requireNonNull(entityFactories);
		this.entityFactories = entityFactories;
	}

	public static Validation with() {
		return DEFAULT;
	}

	public static Validation spring() {
		return with(new SpringValidatorFactory());
	}

	public static Validation with(IEntityFactory<IValidator> factory) {
		return new Validation(factory);
	}

	/**
	 * 校验所有的结果异常
	 *
	 * @param obj 对象
	 * @return 合并后的校验结果
	 */
	public ValidationResult checkAll(Object obj) {
		Objects.requireNonNull(obj);
		List<ValidationResult> results = new ArrayList<>();
		Optional.ofNullable(checkObject(obj))
				.ifPresent(results::add);
		for (Field field : obj.getClass().getDeclaredFields()) {
			try {
				Object fieldValue = field.get(obj);
				Arrays.stream(field.getAnnotations())
						.map(Annotation::annotationType)
						.collect(Collectors.toSet())
						.forEach(annotation -> {
							Optional.ofNullable(checkField(fieldValue, field, annotation))
									.ifPresent(results::add);
						});
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return ValidationResult.merge(results);
	}

	/**
	 * 校验异常，但不抛出异常，仅返回第一个校验失败的校验结果
	 *
	 * @param obj 对象
	 * @return 第一个校验失败的校验结果
	 */
	public ValidationResult checkFirstFailed(Object obj) {
		Objects.requireNonNull(obj);
		ValidationResult result = null;
		result = checkObject(obj);
		if (result != null && result.isFail()) {
			return result;
		}
		for (Field field : obj.getClass().getDeclaredFields()) {
			try {
				Object fieldValue = field.get(obj);
				Set<? extends Class<? extends Annotation>> annotationSize = Arrays.stream(field.getAnnotations())
						.map(Annotation::annotationType).collect(Collectors.toSet());
				for (Class<? extends Annotation> annotation : annotationSize) {
					result = checkField(fieldValue, field, annotation);
					if (result != null && result.isFail()) {
						return result;
					}
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return ValidationResult.ok();
	}

	/**
	 * 校验结果并抛出第一个校验结果的异常
	 *
	 * @param obj 对象
	 * @throws ValidationException 校验异常
	 */
	public void checkFirstFailedAndThrow(Object obj) throws ValidationException {
		Objects.requireNonNull(checkFirstFailed(obj)).check();
	}

	/**
	 * 校验所有失败的结果并合并后抛出
	 *
	 * @param obj 对象
	 * @throws ValidationException 校验异常
	 */
	public void checkAllFailedAndThrow(Object obj) throws ValidationException {
		Objects.requireNonNull(checkAll(obj)).check();
	}

	/**
	 * 校验类的对象
	 *
	 * @param obj 对象
	 * @return 校验结果
	 */
	private ValidationResult checkObject(Object obj) {
		CustomValidation customValidation = obj.getClass().getAnnotation(CustomValidation.class);
		if (customValidation == null) {
			return null;
		}
		IValidator validator = Optional.of(customValidation)
				.map(CustomValidation::validator)
				.map(entityFactories::get)
				.orElseThrow(() -> new RuntimeException("no available validator"));
		return validator.validate(obj);
	}

	/**
	 * 属性校验
	 *
	 * @param fieldValue 属性的值
	 * @param field      字段
	 * @param annotation 字段的对应注解
	 * @return 对一个属性的校验结果
	 */
	private ValidationResult checkField(Object fieldValue, Field field, Class<? extends Annotation> annotation) {
		ValidationResult result = null;
		if (annotation.equals(AssertFalse.class)) {
			result = checkField(fieldValue, field, AssertFalse.class, new BooleanAssertFalseValidator()::validate);
		} else if (annotation.equals(AssertTrue.class)) {
			result = checkField(fieldValue, field, AssertTrue.class, new BooleanAssertTrueValidator()::validate);
		} else if (annotation.equals(AssertNull.class)) {
			result = checkField(fieldValue, field, AssertNull.class, new ObjectNullValidator()::validate);
		} else if (annotation.equals(AssertNotNull.class)) {
			result = checkField(fieldValue, field, AssertNotNull.class, new ObjectNotNullValidator()::validate);
		} else if (annotation.equals(DateFuture.class)) {
			result = checkField(fieldValue, field, DateFuture.class, new DateFutureValidator()::validate);
		} else if (annotation.equals(DatePast.class)) {
			result = checkField(fieldValue, field, DatePast.class, new DatePastValidator()::validate);
		} else if (annotation.equals(DecimalMax.class)) {
			result = checkField(fieldValue, field, DecimalMax.class, new DecimalMaxValidator()::validate);
		} else if (annotation.equals(DecimalMin.class)) {
			result = checkField(fieldValue, field, DecimalMin.class, new DecimalMinValidator()::validate);
		} else if (annotation.equals(DecimalRange.class)) {
			result = checkField(fieldValue, field, DecimalRange.class, new DecimalRangeValidator()::validate);
		} else if (annotation.equals(StringLength.class)) {
			result = checkField(fieldValue, field, StringLength.class, new StringLengthValidator()::validate);
		} else if (annotation.equals(StringNotEmpty.class)) {
			result = checkField(fieldValue, field, StringNotEmpty.class, new StringNotEmptyValidator()::validate);
		} else if (annotation.equals(StringPattern.class)) {
			result = checkField(fieldValue, field, StringPattern.class, new StringPatternValidator()::validate);
		} else if (annotation.equals(CustomValidation.class)) {
			CustomValidation customValidation = field.getAnnotation(CustomValidation.class);
			IValidator validator = Optional.ofNullable(customValidation)
					.map(CustomValidation::validator)
					.map(entityFactories::get)
					.orElseThrow(() -> new RuntimeException("no available validator"));
			result = validator.validate(fieldValue);
		}
		return result;
	}

	/**
	 * 属性校验
	 *
	 * @param field      字段
	 * @param fieldValue 字段的值
	 * @param clazz      注解的对应类型
	 * @param validator  对应的校验方法
	 * @param <A>        注解泛型类型
	 * @return 校验结果
	 */
	private <A extends Annotation> ValidationResult checkField(Object fieldValue, Field field, Class<A> clazz,
	                                                           BiFunction<Object, A, ValidationResult> validator) {
		A required = field.getAnnotation(clazz);
		return validator.apply(fieldValue, required);
	}
}
