package cn.tangyujun.delines.demo;

import cn.tangyujun.delines.decoder.IDelinesDecoder;

import java.util.regex.Matcher;

public class IsOffInParser implements IDelinesDecoder {
	@Override
	public <T> T decode(Matcher result, Class<T> targetClazz) {
		if(Boolean.class.equals(targetClazz)){
			if (result.find()){
				return (T) Boolean.valueOf("A".equals(result.group()));
			}
		}
		return null;
	}
}
