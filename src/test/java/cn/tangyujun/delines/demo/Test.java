package cn.tangyujun.delines.demo;

import cn.tangyujun.delines.Delines;

public class Test {
	public static void main(String[] args) {
		String[] lines = new String[]{"P01 小明 14 M", "P02 小霞 15 F"};
		Person person = null;
		for (String line : lines) {
			person = Delines.with(line, Person.class);
			System.out.println(person);
		}
	}
}
