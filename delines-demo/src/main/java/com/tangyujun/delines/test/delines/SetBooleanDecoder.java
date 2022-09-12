package com.tangyujun.delines.test.delines;

import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.decoder.ListObjectDecoder;
import com.tangyujun.delines.decoder.SetObjectDecoder;

public class SetBooleanDecoder extends SetObjectDecoder<Boolean> {
	@Override
	public Boolean decode(String data, DelinesBusField field) {
		return "y".equalsIgnoreCase(data);
	}
}
