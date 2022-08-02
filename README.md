# delines
解析各种文本（单行或多行）文件转Java实体

### 示例

```java
// 单行文本转换样例
public static void main(String[] args) {
    String[] lines = new String[]{"P01 小明 14 M", "P02 小霞 15 F"};
    Person person = null;
    for (String line : lines) {
        person = Delines.with(line, Person.class);
        System.out.println(person);
    }
}
// Person{id=1, name='小明', sex='M', isMan=true}
// Person{id=2, name='小霞', sex='F', isMan=false}
```

### 单行文本转实体
- [x] 多种基础数据类型支持
- [x] 支持自定义decoder
```java
public class Person implements IDelinesEntity {

	@DelinesField(regExp = "(?<=P)\\d+")
	private Integer id;

	@DelinesField(regExp = "[\\u4e00-\\u9fa5]+")
	private String name;

	@DelinesField(regExp = "(?<=\\s)[FM]$")
	private String sex;

	@DelinesField(regExp = "(?<=\\s)[FM]$", decoder = IsManParser.class)
	private Boolean isMan;
	// 省略其他
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

### 多行文本转实体集合
- [x] 指定范围（行或则正则形式）
- [x] 实体对应文本内容校验
- [x] 匹配事件拦截（匹配成功回调执行）
- [x] 多种类型匹配执行，一个文本中有多种类型的行类型
- [ ] 实体内容通用校验


#### 多行文本转实体实例
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
    //...
}
```

```java
String text = "成绩单\n" +
        "P01 小明 14 M 19990903\n" +
        "语文：73\n数学：92\n" +
        "P02 小霞 15 F 19980706\n" +
        "语文：64\n数学：94\n" +
        "P03 小文 15 M 19981212\n" +
        "语文：90\n数学：73\n";
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
document.notifyFounder(Person.class, ((bus, entity) -> {
    print.accept(document);
    return true;
}));
document.consume();
print.accept(document);
```
输出结果样例：
```json
Person{id=1, name='小明', age=14, sex='M', isMan=true, birthday=1999-09-03}[Score{course='语文', score='73'}, Score{course='数学', score='92'}]
Person{id=2, name='小霞', age=15, sex='F', isMan=false, birthday=1998-07-06}[Score{course='语文', score='64'}, Score{course='数学', score='94'}]
Person{id=3, name='小文', age=15, sex='M', isMan=true, birthday=1998-12-12}[Score{course='语文', score='90'}, Score{course='数学', score='73'}]
```

