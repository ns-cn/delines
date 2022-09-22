package com.tangyujun.delines.validation.validator;

import cn.hutool.core.text.CharSequenceUtil;
import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.DatePast;

import javax.naming.OperationNotSupportedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * 时间之后的校验器
 */
public class DatePastValidator implements IInnerValidator<DatePast> {

	@Override
	public ValidationResult validate(Object data, DatePast required) {
		if (Objects.isNull(data)) {
			return ValidationResult.fail(required.message());
		}
		if (data instanceof LocalDateTime) {
			LocalDateTime time = (LocalDateTime) data;
			LocalDateTime baseLine = CharSequenceUtil.isEmpty(required.value()) ? LocalDateTime.now() :
					LocalDateTime.parse(required.value(), DateTimeFormatter.ofPattern(required.format()));
			return (required.contain() ? !time.isAfter(baseLine) : baseLine.isAfter(time)) ?
					ValidationResult.ok() : ValidationResult.fail(required.message());
		}
		if (data instanceof Date) {
			long time = ((Date) data).getTime();
			long baseLine;
			try {
				baseLine = CharSequenceUtil.isEmpty(required.value()) ? new Date().getTime() :
						new SimpleDateFormat(required.format()).parse(required.value()).getTime();
				return (required.contain() ? time <= baseLine : time < baseLine) ?
						ValidationResult.ok() : ValidationResult.fail(required.message());
			} catch (ParseException e) {
				// 请引用delines-checker在编译期进行注解检查
				throw new RuntimeException(e);
			}
		}
		if (data instanceof LocalDate) {
			LocalDate time = (LocalDate) data;
			LocalDate baseLine = CharSequenceUtil.isEmpty(required.value()) ? LocalDate.now() :
					LocalDate.parse(required.value(), DateTimeFormatter.ofPattern(required.format()));
			return (required.contain() ? !time.isAfter(baseLine) : baseLine.isAfter(time)) ?
					ValidationResult.ok() : ValidationResult.fail(required.message());
		}
		if (data instanceof LocalTime) {
			LocalTime time = (LocalTime) data;
			LocalTime baseLine = CharSequenceUtil.isEmpty(required.value()) ? LocalTime.now() :
					LocalTime.parse(required.value(), DateTimeFormatter.ofPattern(required.format()));
			return (required.contain() ? !time.isAfter(baseLine) : baseLine.isAfter(time)) ?
					ValidationResult.ok() : ValidationResult.fail(required.message());
		}
		try {
			throw new OperationNotSupportedException();
		} catch (OperationNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}
