package com.tangyujun.delines.test.nested;

import com.tangyujun.delines.AbstractDelinesEntity;
import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.annotation.DelinesNestedField;

import java.time.LocalTime;
import java.util.List;

/**
 * P14 CES6616/6615 W/Z B6423 A319 哈尔滨 1400 1615 烟台 0940 1135 哈尔滨
 * P15 CES2199/2200 W/Z B6423/B6430 A319 西安 0615 0830 烟台 0940 1200 西安
 */
public class PlanFlightTextMapper extends AbstractDelinesEntity {

	@DelinesField("P\\d*\\b")
	private String number;

	@DelinesField("\\b\\w{3}(?=\\d{3,4})")
	private String airlines;

	@DelinesField("(?<=\\w{3})\\d{3,4}")
	private String aFlightNo;
	@DelinesField("(?<=\\w{3}\\d{3,4}/)(\\d{3,4})")
	private String dFlightNo;
	@DelinesField("\\b\\w/\\w\\b")
	private String task;
	@DelinesField("(?<=\\b\\w/\\w\\s)[\\w/]*\\b")
	private String craftNo;
	@DelinesField("\\w{3,5}(?=\\s[\\u4e00-\\u9fa5])")
	private String craftType;
	@DelinesNestedField("[\\u4e00-\\u9fa5][\\u4e00-\\u9fa5\\s\\d]*[\\u4e00-\\u9fa5]")
	private Station station;

	public static class Station {
		/**
		 * 始发机场
		 */
		@DelinesField("^[\\u4e00-\\u9fa5]{2,4}")
		private String departureAirport;
		/**
		 * 始发时刻
		 */
		@DelinesField(value = "\\d{4}", dateFormat = "HHmm")
		private LocalTime departureTime;
		/**
		 * 经停站
		 */
		@DelinesNestedField(value = "\\d{4}\\s[\\u4e00-\\u9fa5]{2,4}\\s\\d{4}")
		private List<Alt> alts;
		/**
		 * 到达时刻
		 */
		@DelinesField(value = "\\d{4}(?=\\s[\\u4e00-\\u9fa5]{2,4}$)", dateFormat = "HHmm")
		private LocalTime arriveTime;
		/**
		 * 达到站
		 */
		@DelinesField("[\\u4e00-\\u9fa5]{2,4}$")
		private String arriveAirport;

		public static class Alt {
			@DelinesField(value = "^\\d{4}", dateFormat = "HHmm")
			private LocalTime departureTime;
			@DelinesField("[\\u4e00-\\u9fa5]{2,5}")
			private String airport;
			@DelinesField(value = "\\d{4}$", dateFormat = "HHmm")
			private LocalTime arriveTime;
		}
	}
}
