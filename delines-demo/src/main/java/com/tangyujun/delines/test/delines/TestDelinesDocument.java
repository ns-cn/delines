package com.tangyujun.delines.test.delines;

import com.tangyujun.delines.DelinesDocument;

import java.util.Optional;

public class TestDelinesDocument {

	public static void main(String[] args) {
		DelinesDocument document = DelinesDocument.of("123 456 123 YNyn\n147 258 258 nyNY")
				.registerDelinesEntity(Data.class)
				.consume();
		Optional.ofNullable(document.getFoundEntities(Data.class))
				.ifPresent(t -> t.forEach(System.out::println));
	}
}
