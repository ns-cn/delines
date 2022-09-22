package com.tangyujun.delines.demo.delines;

import com.tangyujun.delines.Delines;
import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.annotation.DelinesNestedField;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class TestDelines {

	@Test
	public void testDelines(){
		Data result = Delines.with("123 123 20220922", Data.class);
		Assert.assertFalse(Objects.isNull(result));
		Assert.assertFalse(result.number.isEmpty());
		Assert.assertFalse(result.dates.isEmpty());
		Assert.assertNotNull(result.date);
		Assert.assertNotNull(result.date.year);
		Assert.assertNotNull(result.date.day);
		Assert.assertNotNull(result.date.month);
		Assert.assertEquals(result.date.year, Integer.valueOf(2022));
		Assert.assertEquals(result.date.month, Integer.valueOf(9));
		Assert.assertEquals(result.date.day, Integer.valueOf(22));
	}

	public static class Data{
		@DelinesField("\\d{3}\\b")
		private List<Integer> number;

		// 不严谨的写法,仅用于测试
		@DelinesField(value = "\\d{8}",dateFormat = "yyyyMMdd")
		private List<LocalDate> dates;


		@DelinesNestedField("\\d{8}")
		private CustomDate date;
	}

	public static class CustomDate{
		@DelinesField("\\d{4}")
		private Integer year;

		@DelinesField("(?<=\\d{4})\\d{2}")
		private Integer month;

		@DelinesField("\\d{2}$")
		private Integer day;
	}
}
