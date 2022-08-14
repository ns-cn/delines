# delines
基于正则匹配和反射实现解析各种文本（单行或多行）文件转Java实体

## 特性介绍
- [x] 特性1、多种基础数据类型支持
- [x] 特性2、可自定义类型，支持自定义decoder，支持@EntityCreator方式生成实例，可使用Spring Bean方式
- [x] 特性3、支持单行文本与多种映射实体匹配执行
- [x] 特性4、支持模型应用范围指定（指定行行或则指定正则形式）
- [x] 特性5、支持单行和文本的流和字符串文本的映射解析
- [x] 特性6、支持匹配事件拦截（匹配成功回调执行）
- [x] 特性7、原生级嵌套支持
- [x] 特性8、支持编译过程的校验（正则格式校验，必填项校验，@EntityCreator校验(since V1.0.2)）
```shell
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ delines-spring ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 4 source files to /Users/tangyujun/workspace/java/delines/delines-spring/target/test-classes
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /Users/tangyujun/workspace/java/delines/delines-spring/src/test/java/Score.java:[10,25] @DelinesField with wrong pattern: \b\d{1,3}\b[]
[ERROR] /Users/tangyujun/workspace/java/delines/delines-spring/src/test/java/Person.java:[8,8] @DelinesEntity with wrong pattern: P\d+.*[]
[INFO] 2 errors 
[INFO] -------------------------------------------------------------
```

### 单行文本转换示例
```java
// 单行文本转换样例
public class TestDelines {
	public static void main(String[] args) {
		String[] lines = new String[]{"P01 小明 14 M 19990909 1999年9月9日", "P02 小霞 15 F 19990919 1999年09月19日"};
		Person person = null;
		for (String line : lines) {
			person = Delines.with(line, Person.class);
			System.out.println(person);
		}
	}
}
// Person{id=1, name='小明', age=14, sex='M', isMan=true, birthday=1999-09-09, birth=1999-09-09}
// Person{id=2, name='小霞', age=15, sex='F', isMan=false, birthday=1999-09-19, birth=1999-09-19}
```
[Person.java](./delines/src/test/java/com/tangyujun/delines/demo/Person.java)和
[MyDate.java](./delines/src/test/java/com/tangyujun/delines/demo/MyDate.java)
```java
// 自定义映射实体（自定义解析和嵌套定义实例）
@DelinesEntity(required = "P\\d+.*")
public class Person extends AbstractDelinesEntity {
	@DelinesField(regExp = "(?<=P)\\d+")
	private Integer id;
	@DelinesField(regExp = "[\\u4e00-\\u9fa5]+")
	private String name;
	@DelinesField(regExp = "\\b\\d{1,3}\\b")
	private Integer age;
	@DelinesField(regExp = "\\b[FM]\\b")
	private String sex;
	@DelinesField(regExp = "\\b[FM]\\b", decoder = IsManParser.class)
	private Boolean isMan;
	@DelinesField(regExp = "\\b[0-9]{8}", dateFormat = "yyyyMMdd", decodeExceptionHandler = ParseExceptionHandler.class)
	private LocalDate birthday;
	@DelinesNestedField // 声明一个嵌套的对象
	private MyDate birth;
	//...
}
public class MyDate {
	@DelinesField(regExp = "\\b[0-9]{4}(?=年)", dateFormat = "yyyyMMdd")
	private Integer year;
	@DelinesField(regExp = "(?<=年)[0-9]{1,2}(?=月)", dateFormat = "yyyyMMdd")
	private Integer month;
	@DelinesField(regExp = "(?<=月)[0-9]{1,2}(?=日)", dateFormat = "yyyyMMdd")
	private Integer day;
}
```

```java
// 自定义内容转换器
public class IsManParser implements IDelinesDecoder {
	@Override
	public <T> T decode(Matcher result, DelinesBusField field) {
		if(Boolean.class.equals(field.getResultType())){
			if (result.find()){
				return (T) Boolean.valueOf("M".equals(result.group()));
			}
		}
		return null;
	}
}
```

## 多行文本转实体实例
本示例将多行文本读取学成成绩并打印
```java
// 实体定义
// 从至少第1行开始读取，并且严格满足设定正则才匹配
@DelinesEntity(rangeStartType = DelinesEntity.RangeType.NUMBER, rangeStart = "1", required = "[\\u4e00-\\u9fa5]+.*")
public class Score extends AbstractDelinesEntity {
	@DelinesField(regExp = "[\\u4e00-\\u9fa5]+")
	private String course;
	@DelinesField(regExp = "\\b\\d{1,3}\\b")
	private String score;
}
@DelinesEntity(required = "P\\d+.*")
public class Person extends AbstractDelinesEntity {
	//... ...
}
```

```java
public class TestDelinesDocument {
	public static void main(String[] args) {
		String text = "成绩单\n" +
				"P01 小明 14 M 19990903\n" +
				"语文 73\n数学 92\n" +
				"P02 小霞 15 F 19980706\n" +
				"语文 64\n数学 94\n" +
				"P03 小文 15 M 19981212\n" +
				"语文 90\n数学 73\n";
		DelinesDocument document = DelinesDocument.of(text)
				.registerDelinesEntity(Person.class)
				.registerDelinesEntity(Score.class);
		Consumer<DelinesDocument> print = (doc) -> {
			List<Person> people = document.getFoundEntities(Person.class);
			if (people != null) {
				Person person = people.get(people.size() - 1);
				List<Score> scores = document.getFoundEntities(Score.class);
				System.out.printf("%s%s\n", person.toString(), scores);
				document.getBusEntity(Score.class).cleanEntities();
			}
		};
		document.addNotifier(Person.class, ((bus, entity) -> {
			print.accept(document);
			return true;
		})).consume();
		print.accept(document);
	}
}
```
输出结果样例：
```text
Person{id=1, name='小明', age=14, sex='M', isMan=true, birthday=1999-09-03, birth=null-null-null}[Score{course='语文', score=73}, Score{course='数学', score=92}]
Person{id=2, name='小霞', age=15, sex='F', isMan=false, birthday=1998-07-06, birth=null-null-null}[Score{course='语文', score=64}, Score{course='数学', score=94}]
Person{id=3, name='小文', age=15, sex='M', isMan=true, birthday=1998-12-12, birth=null-null-null}[Score{course='语文', score=90}, Score{course='数学', score=73}]
```
## [计划支持](./PLAN_VERSION.md)
详情参见[版本历史](./PLAN_VERSION.md)