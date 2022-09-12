package com.tangyujun.delines.test.delines;

import com.tangyujun.delines.Delines;

public class TestDelines {
	public static void main(String[] args) {
		Data with = Delines.with("123 456 123 YNyn", Data.class);
		System.out.println(with);
	}
}
