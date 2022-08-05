package com.tangyujun.delines.demo;

import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.decoder.IDelinesDecoder;

import java.util.regex.Matcher;

public class IsManParser implements IDelinesDecoder {
	@Override
	public Object decode(Matcher result, DelinesBusField field) {
		if(Boolean.class.equals(field.getResultType())){
			if (result.find()){
				return Boolean.valueOf("M".equals(result.group()));
			}
		}
		return null;
	}
}
