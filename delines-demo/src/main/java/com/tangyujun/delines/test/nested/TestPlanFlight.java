package com.tangyujun.delines.test.nested;

import com.tangyujun.delines.Delines;

public class TestPlanFlight {

	public static void main(String[] args) {
		String data = "P15 CES2199/2200 W/Z B6423/B6430 A319 西安 0615 0830 烟台 0940 1200 西安";
		PlanFlightTextMapper mapper = Delines.with(data, PlanFlightTextMapper.class);
		System.out.println(mapper);
	}
}
