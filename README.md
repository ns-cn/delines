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
- [ ] 指定范围（行或则正则形式）
- [ ] 实体对应文本内容校验
- [ ] 匹配事件拦截（匹配成功回调执行）




