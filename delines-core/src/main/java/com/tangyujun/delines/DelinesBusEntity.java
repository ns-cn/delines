package com.tangyujun.delines;

import com.tangyujun.delines.annotation.DelinesEntity;
import com.tangyujun.delines.handler.Notifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 目标行类型bus装载类，包含类型的基础数据
 * @param <T>
 */
public class DelinesBusEntity<T extends IDelinesEntity> {

	private Class<T> clazz;

	private Pattern required;
	private DelinesEntity.RangeType rangeStartType;
	private DelinesEntity.RangeType rangeEndType;
	private DelinesEntity.RangeBorder rangeStartBorder;
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

	private List<DelinesBusField> fields;

	private List<Notifier<T>> notifiers;

	private List<T> entities;

	public static <T extends IDelinesEntity> DelinesBusEntity<T> of(Class<T> clazz, List<DelinesBusField> fields) {
		DelinesBusEntity<T> entity = new DelinesBusEntity<>();
		entity.setClazz(clazz);
		entity.setFields(fields);
		return entity;
	}

	public T create() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void add(IDelinesEntity t) {
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

	public void addNotifier(Notifier<T> notifier){
		if (notifier == null) {
			return;
		}
		if (notifiers == null) {
			notifiers = new ArrayList<>();
		}
		notifiers.add(notifier);
	}

	public void cleanEntities() {
		Optional.ofNullable(entities).ifPresent(List::clear);
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public List<DelinesBusField> getFields() {
		return fields;
	}

	public void setFields(List<DelinesBusField> fields) {
		this.fields = fields;
	}

	public Pattern getRequired() {
		return required;
	}

	public void setRequired(Pattern required) {
		this.required = required;
	}

	public DelinesEntity.RangeType getRangeStartType() {
		return rangeStartType;
	}

	public void setRangeStartType(DelinesEntity.RangeType rangeStartType) {
		this.rangeStartType = rangeStartType;
	}

	public DelinesEntity.RangeType getRangeEndType() {
		return rangeEndType;
	}

	public void setRangeEndType(DelinesEntity.RangeType rangeEndType) {
		this.rangeEndType = rangeEndType;
	}

	public DelinesEntity.RangeBorder getRangeStartBorder() {
		return rangeStartBorder;
	}

	public void setRangeStartBorder(DelinesEntity.RangeBorder rangeStartBorder) {
		this.rangeStartBorder = rangeStartBorder;
	}

	public DelinesEntity.RangeBorder getRangeEndBorder() {
		return rangeEndBorder;
	}

	public void setRangeEndBorder(DelinesEntity.RangeBorder rangeEndBorder) {
		this.rangeEndBorder = rangeEndBorder;
	}

	public Long getRangeStartLine() {
		return rangeStartLine;
	}

	public void setRangeStartLine(Long rangeStartLine) {
		this.rangeStartLine = rangeStartLine;
	}

	public Long getRangeEndLine() {
		return rangeEndLine;
	}

	public void setRangeEndLine(Long rangeEndLine) {
		this.rangeEndLine = rangeEndLine;
	}

	public Pattern getRangeStartPattern() {
		return rangeStartPattern;
	}

	public void setRangeStartPattern(Pattern rangeStartPattern) {
		this.rangeStartPattern = rangeStartPattern;
	}

	public Pattern getRangeEndPattern() {
		return rangeEndPattern;
	}

	public void setRangeEndPattern(Pattern rangeEndPattern) {
		this.rangeEndPattern = rangeEndPattern;
	}

	public List<T> getEntities() {
		return entities;
	}

	public List<Notifier<T>> getNotifiers() {
		return notifiers;
	}
}
