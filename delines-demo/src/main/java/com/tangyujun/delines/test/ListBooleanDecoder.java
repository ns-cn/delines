package com.tangyujun.delines.test;

import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.decoder.ListObjectDecoder;

public class ListBooleanDecoder extends ListObjectDecoder<Boolean> {
	@Override
	public Boolean decode(String data, DelinesBusField field) {
		return "y".equalsIgnoreCase(data);
	}
}
