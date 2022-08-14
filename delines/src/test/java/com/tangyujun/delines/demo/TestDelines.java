package com.tangyujun.delines.demo;

import com.tangyujun.delines.Delines;

public class TestDelines {

	public static void main(String[] args) {
		String[] lines = new String[]{"P01 小明 14 M 19990909 1999年9月9日", "P02 小霞 15 F 19990919 1999年09月19日"};
		Person person = null;
		for (String line : lines) {
			person = Delines.with(line, Person.class);
			System.out.println(person);
		}
	}
}
