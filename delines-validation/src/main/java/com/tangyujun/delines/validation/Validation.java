package com.tangyujun.delines.validation;

import com.tangyujun.delines.decoder.IEntityFactory;
import com.tangyujun.delines.validation.annotation.*;
import com.tangyujun.delines.validation.factories.DefaultValidatorFactory;
import com.tangyujun.delines.validation.validator.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 校验操作
 */
public final class Validation {

	private IEntityFactory<? extends IValidator> entityFactories = new DefaultValidatorFactory();

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

	private Validation(IEntityFactory<? extends IValidator> entityFactories) {
		Objects.requireNonNull(entityFactories);
		this.entityFactories = entityFactories;
	}

	public static Validation with() {
		return DEFAULT;
	}

	public static Validation with(IEntityFactory<? extends IValidator> factory) {
		return new Validation(factory);
	}

	public ValidationResult checkFirstFailed(Object obj) {
		Objects.requireNonNull(obj);
		for (Field field : obj.getClass().getDeclaredFields()) {
			try {
				Object fieldValue = field.get(obj);
				Set<? extends Class<? extends Annotation>> annotationSize = Arrays.stream(field.getAnnotations())
						.map(Annotation::annotationType).collect(Collectors.toSet());
				for (Class<? extends Annotation> annotation : annotationSize) {
					ValidationResult result = null;
					if (annotation.equals(AssertFalse.class)) {
						result = check(field, fieldValue, AssertFalse.class, new BooleanAssertFalseValidator()::validate);
					} else if (annotation.equals(AssertTrue.class)) {
						result = check(field, fieldValue, AssertTrue.class, new BooleanAssertTrueValidator()::validate);
					} else if (annotation.equals(AssertNull.class)) {
						result = check(field, fieldValue, AssertNull.class, new ObjectNullValidator()::validate);
					} else if (annotation.equals(AssertNotNull.class)) {
						result = check(field, fieldValue, AssertNotNull.class, new ObjectNotNullValidator()::validate);
					} else if (annotation.equals(DateFuture.class)) {
						result = check(field, fieldValue, DateFuture.class, new DateFutureValidator()::validate);
					} else if (annotation.equals(DatePast.class)) {
						result = check(field, fieldValue, DatePast.class, new DatePastValidator()::validate);
					} else if (annotation.equals(DecimalMax.class)) {
						result = check(field, fieldValue, DecimalMax.class, new DecimalMaxValidator()::validate);
					} else if (annotation.equals(DecimalMin.class)) {
						result = check(field, fieldValue, DecimalMin.class, new DecimalMinValidator()::validate);
					} else if (annotation.equals(DecimalRange.class)) {
						result = check(field, fieldValue, DecimalRange.class, new DecimalRangeValidator()::validate);
					} else if (annotation.equals(StringLength.class)) {
						result = check(field, fieldValue, StringLength.class, new StringLengthValidator()::validate);
					} else if (annotation.equals(StringNotEmpty.class)) {
						result = check(field, fieldValue, StringNotEmpty.class, new StringNotEmptyValidator()::validate);
					} else if (annotation.equals(StringPattern.class)) {
						result = check(field, fieldValue, StringPattern.class, new StringPatternValidator()::validate);
					}
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

	private <A extends Annotation> ValidationResult check(Field field, Object fieldValue, Class<A> clazz,
	                                                      BiFunction<Object, A, ValidationResult> validator) {
		A required = field.getAnnotation(clazz);
		return validator.apply(fieldValue, required);
	}

	public void checkFirstFailedAndThrow(Object obj) throws ValidationException {
		Objects.requireNonNull(checkFirstFailed(obj)).check();
	}
}
