package com.tangyujun.delines.test.validation;

import com.tangyujun.delines.validation.Validation;
import com.tangyujun.delines.validation.ValidationException;
import com.tangyujun.delines.validation.ValidationResult;

public class TestValidation {
	public static void main(String[] args) throws ValidationException {
		Validation validation = Validation.with();
		Data d = new Data();
		d.a = -2;
		d.b = 5;
		ValidationResult firstFailed = validation.checkFirstFailed(d);
		System.out.println(firstFailed);
		validation.checkAllFailedAndThrow(d);
	}
}
