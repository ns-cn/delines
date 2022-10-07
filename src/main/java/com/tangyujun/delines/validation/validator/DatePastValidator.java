package com.tangyujun.delines.validation.validator;

import cn.hutool.core.text.CharSequenceUtil;
import com.tangyujun.delines.validation.IInnerValidator;
import com.tangyujun.delines.validation.ValidationResult;
import com.tangyujun.delines.validation.annotation.DateFuture;
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
public class DatePastValidator extends DateValidator implements IInnerValidator<DatePast> {

	@Override
	public ValidationResult validate(Object data, DatePast required) {
		return validate(data, required.value(), required.format(),
				required.contain(), required.message(), true);
	}

	@Override
	public ValidationResult validate(Object data) {
		return null;
	}
}
