package cn.tangyujun.delines.demo;

import cn.tangyujun.delines.Delines;

public class Test {
	public static void main(String[] args) {
		String[] lines = new String[]{"P01 CA1234 A B1526", "P02 CA135 D B6538"};
		DynFlight df = null;
		for (String line : lines) {
			df = Delines.with(line, DynFlight.class);
			System.out.println(df);
		}
	}
}
