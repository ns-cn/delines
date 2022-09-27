package com.tangyujun;

import com.tangyujun.delines.AbstractDelinesEntity;
import com.tangyujun.delines.Delines;
import com.tangyujun.delines.DelinesBusField;
import com.tangyujun.delines.DelinesDocument;
import com.tangyujun.delines.annotation.DelinesEntity;
import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.annotation.DelinesNestedField;
import com.tangyujun.delines.decoder.IDelinesDecoder;
import com.tangyujun.delines.handler.NextStringGetter;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;

public class TestDelinesDocument {

	@Test
	public void testDocument() {
		String lines = "P01 小明 14 M 19990909 1999年9月9日\nP02 小霞 15 F 19990919 1999年09月19日";
		DelinesDocument document = DelinesDocument.of(lines).registerDelinesEntity(Person.class)
				.consume(reader -> {
					try {
						String line = reader.readLine();
						if (line != null) {
							System.out.printf("do something with %s\n", line);
							return NextStringGetter.NextString.of(line);
						}
						return NextStringGetter.NextString.of(null);
					} catch (IOException e) {
						return NextStringGetter.NextString.withError(e);
					}
				});
		List<Person> people = document.getFoundEntities(Person.class);
		people.forEach(System.out::println);
	}


	@DelinesEntity(required = "P\\d+.*")
	public static class Person extends AbstractDelinesEntity {
		@DelinesField(value = "(?<=P)\\d+")
		private Integer id;
		@DelinesField(value = "[\\u4e00-\\u9fa5]+")
		private String name;
		@DelinesField(value = "\\b\\d{1,3}\\b")
		private Integer age;
		@DelinesField(value = "\\b[FM]\\b")
		private String sex;
		@DelinesField(value = "\\b[FM]\\b", decoder = TestDelines.IsManParser.class)
		private Boolean isMan;
		@DelinesField(value = "\\b[0-9]{8}", dateFormat = "yyyyMMdd")
		private LocalDate birthday;
		@DelinesNestedField // 声明一个嵌套的对象
		private TestDelines.MyDate birth;

		@Override
		public String toString() {
			return "Person{" +
					"id=" + id +
					", name='" + name + '\'' +
					", age=" + age +
					", sex='" + sex + '\'' +
					", isMan=" + isMan +
					", birthday=" + birthday +
					", birth=" + birth +
					'}';
		}
	}

	public static class MyDate {
		@DelinesField(value = "\\b[0-9]{4}(?=年)", dateFormat = "yyyyMMdd")
		private Integer year;
		@DelinesField(value = "(?<=年)[0-9]{1,2}(?=月)", dateFormat = "yyyyMMdd")
		private Integer month;
		@DelinesField(value = "(?<=月)[0-9]{1,2}(?=日)", dateFormat = "yyyyMMdd")
		private Integer day;

		@Override
		public String toString() {
			return "MyDate{" +
					"year=" + year +
					", month=" + month +
					", day=" + day +
					'}';
		}
	}

	public static class IsManParser implements IDelinesDecoder {
		@Override
		public Object decode(Matcher result, DelinesBusField field) {
			if (Boolean.class.equals(field.getResultType())) {
				if (result.find()) {
					return Boolean.valueOf("M".equals(result.group()));
				}
			}
			return null;
		}
	}
}
