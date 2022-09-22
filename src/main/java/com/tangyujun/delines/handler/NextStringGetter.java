package com.tangyujun.delines.handler;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 字符串读取方式
 */
@FunctionalInterface
public interface NextStringGetter {
	/**
	 * 字符串读取方式
	 *
	 * @param reader reader
	 * @return NextString
	 */
	NextString nextString(BufferedReader reader);

	/**
	 * 默认的方式:逐行读取
	 *
	 * @return NextStringGetter
	 */
	static NextStringGetter defaultReader() {
		return (reader) -> {
			String value;
			try {
				value = reader.readLine();
				return NextString.of(value);
			} catch (IOException e) {
				return NextString.withError(e);
			}
		};
	}

	/**
	 * 下一个字符串
	 */
	class NextString {
		/**
		 * 字符串的值
		 */
		private String value;
		/**
		 * 读取下一个字符串的异常
		 */
		private Throwable throwable;

		/**
		 * 字符串的值
		 *
		 * @param value 字符串的值
		 */
		public NextString(String value) {
			this.value = value;
		}

		/**
		 * 读取字符串的存在异常
		 *
		 * @param throwable 异常具体信息
		 */
		public NextString(Throwable throwable) {
			this.throwable = throwable;
		}

		/**
		 * 构建下一个字符串
		 *
		 * @param value 字符串
		 * @return NextString
		 */
		public static NextString of(String value) {
			return new NextString(value);
		}

		/**
		 * 构建下一个字符串
		 *
		 * @param throwable 异常
		 * @return NextString
		 */
		public static NextString withError(Throwable throwable) {
			return new NextString(throwable);
		}

		/**
		 * 获取字符串的值
		 *
		 * @return 字符串的值
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 获取读取字符串时的异常
		 *
		 * @return 异常
		 */
		public Throwable getThrowable() {
			return throwable;
		}
	}
}
