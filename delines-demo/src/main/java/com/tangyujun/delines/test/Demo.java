package com.tangyujun.delines.test;

import com.tangyujun.delines.validation.annotation.DecimalMax;
import com.tangyujun.delines.validation.annotation.DecimalMin;

public class Demo {
	@DecimalMin(value = "1",message = "a必须小于1")
	public Integer a;

	@DecimalMax(value = "3",message = "b最大值不超过3")
	public Integer b;
}
