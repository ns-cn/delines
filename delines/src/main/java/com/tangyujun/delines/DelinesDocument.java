package com.tangyujun.delines;

import com.tangyujun.delines.handler.NextStringGetter;
import com.tangyujun.delines.handler.Notifier;
import com.tangyujun.delines.parser.DelinesEntityParser;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DelinesDocument {
	/**
	 * 数据源,可包装各种字符流（网络IO，文件IO，或则内存的字符串）
	 */
	private BufferedReader reader;

	private List<DelinesBusEntity<? extends IDelinesEntity>> busEntities;

	private DelinesEntityParser entityParser;

	/**
	 * 根据{@link BufferedReader}构建待处理的
	 *
	 * @param reader {@link BufferedReader} 数据读取的来源
	 */
	public DelinesDocument(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 根据string内容构建待处理的Delines文档
	 *
	 * @param page 文本文件完整内容体
	 * @return {@link DelinesDocument}
	 */
	public static DelinesDocument of(String page) {
		return of(new BufferedReader(new StringReader(page)));
	}

	/**
	 * 根据{@link BufferedReader}构建待处理的Delines文档
	 *
	 * @param reader {@link BufferedReader}
	 * @return {@link DelinesDocument}
	 */
	public static DelinesDocument of(BufferedReader reader) {
		return new DelinesDocument(reader);
	}

	public DelinesDocument withCustomEntityParser(DelinesEntityParser customEntityParser) {
		this.entityParser = customEntityParser;
		return this;
	}

	public DelinesDocument registerDelinesEntity(Class<? extends IDelinesEntity> clazz) {
		if (clazz == null) {
			throw new RuntimeException("could not register deliens entity with NULL");
		}
		if (busEntities == null) {
			busEntities = new ArrayList<>();
		}
		if (busEntities.stream().anyMatch(t -> t.getClazz().equals(clazz))) {
			throw new RuntimeException("Repeated registration");
		}
		DelinesEntityParser entityParser = Optional.ofNullable(this.entityParser).orElse(DelinesEntityParser.DEFAULT);
		busEntities.add(entityParser.parse(clazz));
		return this;
	}

	public <T extends IDelinesEntity> DelinesDocument addNotifier(Class<T> clazz, Notifier<T> notifier) {
		if (clazz == null || notifier == null) {
			return this;
		}
		DelinesBusEntity<T> bus = getBusEntity(clazz);
		Optional.ofNullable(bus)
				.orElseThrow(() -> new RuntimeException("could not add notifier to unregistered Class" + clazz + "!"));
		bus.addNotifier(notifier);
		return this;
	}

	/**
	 * 实际消费执行内容，使用默认的读取行的方式
	 */
	public DelinesDocument consume() {
		return consume(NextStringGetter.defaultReader());
	}

	/**
	 * 实际消费执行内容
	 *
	 * @param getter 自定义文本获取方式
	 */
	public DelinesDocument consume(NextStringGetter getter) {
		if (getter == null) {
			throw new RuntimeException("fail to consume with no getter assigned!");
		}
		if (busEntities == null || busEntities.size() == 0) {
			return this;
		}
		NextStringGetter.NextString next;
		int index = 0;  // 行索引
		for (; ; ) {
			next = getter.nextString(reader);
			if (next == null || next.getValue() == null || next.getThrowable() != null) {
				break;
			}
			String data = next.getValue();
			for (DelinesBusEntity<? extends IDelinesEntity> bus : busEntities) {
				IDelinesEntity value = Delines.with(DelinesLine.of(index, data), bus);
				Optional.ofNullable(value).ifPresent(t -> {
					boolean addToBus = true;
					List<? extends Notifier<? extends IDelinesEntity>> clazzNotifiers = bus.getNotifiers();
					if (clazzNotifiers != null) {
						for (Notifier clazzNotifier : clazzNotifiers) {
							addToBus = addToBus && clazzNotifier.notify(bus, t);
						}
					}
					if (addToBus) {
						bus.add(t);
					}
				});
			}
			index++;
		}
		return this;
	}

	public <T extends IDelinesEntity> DelinesBusEntity<T> getBusEntity(Class<T> clazz) {
		if(busEntities==null){
			return null;
		}
		for (DelinesBusEntity<? extends IDelinesEntity> busEntity : busEntities) {
			if (busEntity.getClazz().equals(clazz)) {
				return (DelinesBusEntity<T>) busEntity;
			}
		}
		return null;
	}

	public <T extends IDelinesEntity> List<T> getFoundEntities(Class<T> clazz) {
		if(busEntities==null){
			return null;
		}
		for (DelinesBusEntity<? extends IDelinesEntity> busEntity : busEntities) {
			if (busEntity.getClazz().equals(clazz)) {
				return (List<T>) busEntity.getEntities();
			}
		}
		return null;
	}

	public List<DelinesBusEntity<? extends IDelinesEntity>> getBusEntities() {
		return busEntities;
	}
}
