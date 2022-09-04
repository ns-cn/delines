package com.tangyujun.delines.test;

import com.tangyujun.delines.AbstractDelinesEntity;
import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.validation.annotation.DecimalMax;
import com.tangyujun.delines.validation.annotation.DecimalMin;

import java.util.List;
import java.util.Set;

public class Demo extends AbstractDelinesEntity {
	@DecimalMin(value = "1",message = "a必须小于1")
	public Integer a;

	@DecimalMax(value = "3",message = "b最大值不超过3")
	public Integer b;
	
	@DelinesField("[\\d]{3}")
	public List<Integer> lists;

	@DelinesField("[\\d]{3}")
	public Set<Integer> sets;

	@Override
	public String toString() {
		return "Demo{" +
				"lists=" + lists +
				", sets=" + sets +
				'}';
	}
}
