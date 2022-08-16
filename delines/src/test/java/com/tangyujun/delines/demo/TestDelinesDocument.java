package com.tangyujun.delines.demo;

import com.tangyujun.delines.DelinesDocument;

import java.util.List;
import java.util.function.Consumer;

public class TestDelinesDocument {
	public static void main(String[] args) {
		String text = "成绩单\n" +
				"P01 小明 14 M 19990903\n" +
				"语文 73\n数学 92\n" +
				"P02 小霞 15 F 19980706\n" +
				"语文 64\n数学 94\n" +
				"P03 小文 15 M 19981212\n" +
				"语文 90\n数学 73\n";
		DelinesDocument document = DelinesDocument.of(text);
		Consumer<DelinesDocument> print = (doc) -> {
			List<Person> people = document.getFoundEntities(Person.class);
			if (people != null) {
				Person person = people.get(people.size() - 1);
				List<Score> scores = document.getFoundEntities(Score.class);
				System.out.printf("%s%s\n", person.toString(), scores);
				document.getBusEntity(Score.class).cleanEntities();
			}
		};
		document.registerDelinesEntity(Person.class, ((bus, entity) -> {
					print.accept(document);
					return true;
				}))
				.registerDelinesEntity(Score.class)
				.consume();
		print.accept(document);
	}

/*
Person{id=1, name='小明', age=14, sex='M', isMan=true, birthday=1999-09-03}[Score{course='语文', score='73'}, Score{course='数学', score='92'}]
Person{id=2, name='小霞', age=15, sex='F', isMan=false, birthday=1998-07-06}[Score{course='语文', score='64'}, Score{course='数学', score='94'}]
Person{id=3, name='小文', age=15, sex='M', isMan=true, birthday=1998-12-12}[Score{course='语文', score='90'}, Score{course='数学', score='73'}]
*/
}
