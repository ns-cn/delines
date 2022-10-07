package com.tangyujun.delines.validation.validator;

import cn.hutool.core.text.CharSequenceUtil;
import com.tangyujun.delines.validation.ValidationResult;

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
 * 时间类型的校验器
 */
public abstract class DateValidator {

	/**
	 * 实际教研方法
	 *
	 * @param data     具体数据
	 * @param value    注解数据
	 * @param format   时间格式
	 * @param contain  是否包含标注时间点
	 * @param message  提示消息
	 * @param datePast 是否必须是过去的时间
	 * @return 校验结果
	 */
	protected ValidationResult validate(Object data, String value, String format, boolean contain, String message,
	                                    boolean datePast) {
		if (Objects.isNull(data)) {
			return ValidationResult.fail(message);
		}
		if (data instanceof LocalDateTime) {
			LocalDateTime time = (LocalDateTime) data;
			LocalDateTime baseLine = CharSequenceUtil.isEmpty(value) ? LocalDateTime.now() :
					LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format));
			boolean success = (datePast ? time.isBefore(baseLine) : baseLine.isBefore(time))
					|| (contain && time.equals(baseLine));
			return success ? ValidationResult.ok() : ValidationResult.fail(message);
		}
		if (data instanceof Date) {
			long time = ((Date) data).getTime();
			long baseLine;
			try {
				baseLine = CharSequenceUtil.isEmpty(value) ? new Date().getTime() :
						new SimpleDateFormat(format).parse(value).getTime();
				boolean success = (datePast ? time < baseLine : time > baseLine)
						|| (contain && time == baseLine);
				return success ? ValidationResult.ok() : ValidationResult.fail(message);
			} catch (ParseException e) {
				// 请引用delines-checker在编译期进行注解检查
				throw new RuntimeException(e);
			}
		}
		if (data instanceof LocalDate) {
			LocalDate time = (LocalDate) data;
			LocalDate baseLine = CharSequenceUtil.isEmpty(value) ? LocalDate.now() :
					LocalDate.parse(value, DateTimeFormatter.ofPattern(format));
			boolean success = (datePast ? time.isBefore(baseLine) : baseLine.isBefore(time))
					|| (contain && time.equals(baseLine));
			return success ? ValidationResult.ok() : ValidationResult.fail(message);
		}
		if (data instanceof LocalTime) {
			LocalTime time = (LocalTime) data;
			LocalTime baseLine = CharSequenceUtil.isEmpty(value) ? LocalTime.now() :
					LocalTime.parse(value, DateTimeFormatter.ofPattern(format));
			boolean success = (datePast ? time.isBefore(baseLine) : baseLine.isBefore(time))
					|| (contain && time.equals(baseLine));
			return success ? ValidationResult.ok() : ValidationResult.fail(message);
		}
		try {
			throw new OperationNotSupportedException();
		} catch (OperationNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
