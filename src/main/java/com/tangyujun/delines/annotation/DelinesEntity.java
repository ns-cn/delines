package com.tangyujun.delines.annotation;


import java.lang.annotation.*;

/**
 * 实体类型注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelinesEntity {

	/**
	 * @return 行文本必须满足的正则表达式条件，默认为空不做限制
	 */
	String required() default "";

	/**
	 * @return 开始范围的指定类型，可选数字或正则<br>
	 * 或设置为{@link RangeType#NONE}不设置开始范围的边界
	 */
	RangeType rangeStartType() default RangeType.NONE;

	/**
	 * @return 开始范围的边界处理方式，默认为包含边界位置的点
	 */
	RangeBorder rangeStartBorder() default RangeBorder.INCLUDE;


	/**
	 * @return 结束范围的指定类型，可选数字或正则<br>
	 * 或设置为{@link RangeType#NONE}不设置结束范围的边界
	 */
	RangeType rangeEndType() default RangeType.NONE;

	/**
	 * @return 结束范围的边界处理方式，默认为包含边界位置的点
	 */
	RangeBorder rangeEndBorder() default RangeBorder.INCLUDE;

	/**
	 * @return 开始范围，可选择纯数字（指定行）方式或则正则方式
	 * {@link #rangeStartType()} ()}
	 */
	String rangeStart() default "";

	/**
	 * @return 结束范围，可选择纯数字（指定行）方式或正则方式
	 * {@link #rangeEndType()}
	 */
	String rangeEnd() default "";

	/**
	 * 范围的指定方式<br>
	 * {@link RangeType#NONE} ： 不指定范围边界<br>
	 * {@link RangeType#NUMBER} ：范围边界使用所在行数字指定<br>
	 * {@link RangeType#REGULAR} ：范围边界使用正则表达式匹配行<br>
	 */
	enum RangeType {
		/**
		 * 不指定边界
		 */
		NONE,
		/**
		 * 使用行号指定边界
		 */
		NUMBER,
		/**
		 * 使用正则指定边界
		 */
		REGULAR
	}

	/**
	 * 范围边界的处理方式<br>
	 * {@link RangeBorder#INCLUDE} ： 包含边界行<br>
	 * {@link RangeBorder#EXCLUDE} ：不包含边界行<br>
	 */
	enum RangeBorder {
		/**
		 * 包含边界
		 */
		INCLUDE,
		/**
		 * 排除边界
		 */
		EXCLUDE
	}
}
