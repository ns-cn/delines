package com.tangyujun.delines.test;

import com.tangyujun.delines.validation.annotation.DecimalMin;

public class Demo {
	@DecimalMin("1")
	public Integer a;
}
