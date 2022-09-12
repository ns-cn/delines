package com.tangyujun.delines.test.delines;

import com.tangyujun.delines.AbstractDelinesEntity;
import com.tangyujun.delines.annotation.DelinesField;

import java.util.List;
import java.util.Set;

public class Data extends AbstractDelinesEntity {

	@DelinesField("[\\d]{3}")
	public List<Integer> lists;

	@DelinesField("[\\d]{3}")
	public Set<Integer> sets;

	@DelinesField(value = "[ynYN]", decoder = ListBooleanDecoder.class)
	public List<Boolean> booleans;

	@DelinesField(value = "[ynYN]", decoder = SetBooleanDecoder.class)
	public Set<Boolean> booleanSets;

	@Override
	public String toString() {
		return "Data{" +
				"lists=" + lists +
				", sets=" + sets +
				", booleans=" + booleans +
				", booleanSets=" + booleanSets +
				"}";
	}
}
