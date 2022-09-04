package com.tangyujun.delines.test;

import com.tangyujun.delines.Delines;

public class TestDelines {
	public static void main(String[] args) {
		Demo with = Delines.with("123 456 123", Demo.class, null);
		System.out.println(with);
	}
}
