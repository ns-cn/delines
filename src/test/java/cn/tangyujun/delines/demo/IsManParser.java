package cn.tangyujun.delines.demo;

import cn.tangyujun.delines.DelinesBusField;
import cn.tangyujun.delines.decoder.IDelinesDecoder;

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
