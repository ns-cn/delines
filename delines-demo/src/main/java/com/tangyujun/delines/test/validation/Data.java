package com.tangyujun.delines.test.validation;

import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.test.delines.ListBooleanDecoder;
import com.tangyujun.delines.test.delines.SetBooleanDecoder;
import com.tangyujun.delines.validation.annotation.AssertNotNull;
import com.tangyujun.delines.validation.annotation.DecimalMax;
import com.tangyujun.delines.validation.annotation.DecimalMin;

import java.util.List;
import java.util.Set;

public class Data {
	@DecimalMin(value = "1",message = "a必须小于1")
	public Integer a;

	@DecimalMax(value = "3",message = "b最大值不超过3")
	public Integer b;

	@AssertNotNull(message = "不允许为空")
	private Integer c;

	public void setC(Integer c) {
		this.c = c;
	}
}
