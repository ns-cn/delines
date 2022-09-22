package com.tangyujun.delines;

import com.tangyujun.delines.annotation.DelinesEntity;
import com.tangyujun.delines.handler.Notifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 目标行类型bus装载类，包含类型的基础数据
 *
 * @param <T> 解析类型
 */
public class DelinesBusEntity<T> {

	/**
	 * 具体类型
	 */
	private Class<T> clazz;

	/**
	 * 实体对应的字符串限制
	 */
	private Pattern required;
	/**
	 * 限制行的范围开始类型
	 */
	private DelinesEntity.RangeType rangeStartType;
	/**
	 * 限制行的范围结束类型
	 */
	private DelinesEntity.RangeType rangeEndType;
	/**
	 * 限制行的开始边界
	 */
	private DelinesEntity.RangeBorder rangeStartBorder;
	/**
	 * 限制行的结束边界
	 */
	private DelinesEntity.RangeBorder rangeEndBorder;
	/**
	 * 如果是通过指定行的方式，则记录对应的行数
	 */
	private Long rangeStartLine;    //
	/**
	 * 如果是通过指定行的方式，则记录对应的行数
	 */
	private Long rangeEndLine;
	/**
	 * 如果是通过正则表达式的方式，则生成对应的pattern
	 */
	private Pattern rangeStartPattern;
	/**
	 * 如果是通过正则表达式的方式，则生成对应的pattern
	 */
	private Pattern rangeEndPattern;

	/**
	 * 所有字段
	 */
	private List<DelinesBusField> fields;

	/**
	 * 注册的所有的notifier
	 */
	private List<Notifier<T>> notifiers;

	/**
	 * 所有匹配到的数据
	 */
	private List<T> entities;

	/**
	 * 根据指定类型和所有字段构建busentity
	 *
	 * @param clazz  类型
	 * @param fields 所有解析后的字段
	 * @param <T>    具体类型
	 * @return 构建后的busentity
	 */
	public static <T> DelinesBusEntity<T> of(Class<T> clazz, List<DelinesBusField> fields) {
		DelinesBusEntity<T> entity = new DelinesBusEntity<>();
		entity.setClazz(clazz);
		entity.setFields(fields);
		return entity;
	}

	/**
	 * 创建一个对应类型的实体
	 *
	 * @return 对应类型的实体
	 */
	public T create() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加一个匹配的对象实体
	 *
	 * @param t 匹配的对象实体
	 */
	public void add(Object t) {
		if (t == null) {
			return;
		}
		if (entities == null) {
			entities = new ArrayList<>();
		}
		if (!getClazz().equals(t.getClass())) {
			throw new RuntimeException("fail to add entity with unmatched type:" + t.getClass());
		}
		entities.add((T) t);
	}

	/**
	 * 添加一个匹配成功的notifier
	 *
	 * @param notifier notifier
	 */
	public void addNotifier(Notifier<T> notifier) {
		if (notifier == null) {
			return;
		}
		if (notifiers == null) {
			notifiers = new ArrayList<>();
		}
		notifiers.add(notifier);
	}

	/**
	 * 清除所有匹配成功的实体
	 */
	public void cleanEntities() {
		Optional.ofNullable(entities).ifPresent(List::clear);
	}

	/**
	 * 获取对应的实体类型
	 *
	 * @return 对应的实体类型
	 */
	public Class<T> getClazz() {
		return clazz;
	}

	/**
	 * 设置对应的实体类型
	 *
	 * @param clazz 对应的实体类型
	 */
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 获取所有的字段
	 *
	 * @return 获取所有的字段
	 */
	public List<DelinesBusField> getFields() {
		return fields;
	}

	/**
	 * 设置所有解析后的字段
	 *
	 * @param fields 解析后的字段
	 */
	public void setFields(List<DelinesBusField> fields) {
		this.fields = fields;
	}

	/**
	 * 获取字段对应的字符串要求
	 *
	 * @return 字段对应的字符串正则要求
	 */
	public Pattern getRequired() {
		return required;
	}

	/**
	 * 设置字段对应的字符串要求
	 *
	 * @param required 字段对应的字符串正则要求
	 */
	public void setRequired(Pattern required) {
		this.required = required;
	}

	/**
	 * 设置边界的开始类型
	 *
	 * @return 边界的开始类型
	 */
	public DelinesEntity.RangeType getRangeStartType() {
		return rangeStartType;
	}

	/**
	 * 设置边界的开始类型
	 *
	 * @param rangeStartType 边界的开始类型
	 */
	public void setRangeStartType(DelinesEntity.RangeType rangeStartType) {
		this.rangeStartType = rangeStartType;
	}

	/**
	 * 设置结束边界的类型
	 *
	 * @return 结束边界的类型
	 */
	public DelinesEntity.RangeType getRangeEndType() {
		return rangeEndType;
	}

	/**
	 * 设置结束边界的类型
	 *
	 * @param rangeEndType 结束边界的类型
	 */
	public void setRangeEndType(DelinesEntity.RangeType rangeEndType) {
		this.rangeEndType = rangeEndType;
	}

	/**
	 * 获取开始边界
	 *
	 * @return 开始边界
	 */
	public DelinesEntity.RangeBorder getRangeStartBorder() {
		return rangeStartBorder;
	}

	/**
	 * 设置开始边界
	 *
	 * @param rangeStartBorder 开始边界
	 */
	public void setRangeStartBorder(DelinesEntity.RangeBorder rangeStartBorder) {
		this.rangeStartBorder = rangeStartBorder;
	}

	/**
	 * 获取结束边界
	 *
	 * @return 结束边界
	 */
	public DelinesEntity.RangeBorder getRangeEndBorder() {
		return rangeEndBorder;
	}

	/**
	 * 设置结束边界
	 *
	 * @param rangeEndBorder 结束边界
	 */
	public void setRangeEndBorder(DelinesEntity.RangeBorder rangeEndBorder) {
		this.rangeEndBorder = rangeEndBorder;
	}

	/**
	 * 获取开始行
	 *
	 * @return 开始行
	 */
	public Long getRangeStartLine() {
		return rangeStartLine;
	}

	/**
	 * 设置结束行
	 *
	 * @param rangeStartLine 开始行
	 */
	public void setRangeStartLine(Long rangeStartLine) {
		this.rangeStartLine = rangeStartLine;
	}

	/**
	 * 获取结束行
	 *
	 * @return 结束行
	 */
	public Long getRangeEndLine() {
		return rangeEndLine;
	}

	/**
	 * 设置结束航
	 *
	 * @param rangeEndLine 结束行
	 */
	public void setRangeEndLine(Long rangeEndLine) {
		this.rangeEndLine = rangeEndLine;
	}

	/**
	 * 获取开始行的正则
	 *
	 * @return 开始行的正则
	 */
	public Pattern getRangeStartPattern() {
		return rangeStartPattern;
	}

	/**
	 * 设置开始行的正则
	 *
	 * @param rangeStartPattern 开始行的正则
	 */
	public void setRangeStartPattern(Pattern rangeStartPattern) {
		this.rangeStartPattern = rangeStartPattern;
	}

	/**
	 * 获取结束行的正则
	 *
	 * @return 结束行的正则
	 */
	public Pattern getRangeEndPattern() {
		return rangeEndPattern;
	}

	/**
	 * 设置结束行的正则
	 *
	 * @param rangeEndPattern 结束行的正则
	 */
	public void setRangeEndPattern(Pattern rangeEndPattern) {
		this.rangeEndPattern = rangeEndPattern;
	}

	/**
	 * 获取所有匹配的实体
	 *
	 * @return 匹配成功的实体
	 */
	public List<T> getEntities() {
		return entities;
	}

	/**
	 * 获取所有的notifier
	 *
	 * @return 所有的notifier
	 */
	public List<Notifier<T>> getNotifiers() {
		return notifiers;
	}
}
