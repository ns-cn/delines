package com.tangyujun.delines.handler;

import java.io.BufferedReader;
import java.io.IOException;

@FunctionalInterface
public interface NextStringGetter {
	NextString nextString(BufferedReader reader);

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

	class NextString {
		private String value;
		private Throwable throwable;


		public NextString(String value) {
			this.value = value;
		}

		public NextString(Throwable throwable) {
			this.throwable = throwable;
		}

		public static NextString of(String value) {
			return new NextString(value);
		}

		public static NextString withError(Throwable throwable) {
			return new NextString(throwable);
		}

		public String getValue() {
			return value;
		}

		public Throwable getThrowable() {
			return throwable;
		}
	}
}
