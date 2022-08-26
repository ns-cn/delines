package com.tangyujun.delines.test;

import com.tangyujun.delines.validation.Validation;
import com.tangyujun.delines.validation.ValidationException;

public class Test {
	public static void main(String[] args) throws ValidationException {
		Validation validation = Validation.with();
		Demo d = new Demo();
		d.a = 2;
		validation.checkFirstFailedAndThrow(d);
		d.a = -1;
		validation.checkFirstFailedAndThrow(d);
	}
}
