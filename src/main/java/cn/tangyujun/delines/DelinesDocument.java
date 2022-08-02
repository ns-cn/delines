package cn.tangyujun.delines;

import cn.tangyujun.delines.handler.NextStringGetter;
import cn.tangyujun.delines.handler.Notifier;
import cn.tangyujun.delines.parser.DelinesEntityParser;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelinesDocument {
	/**
	 * 数据源,可包装各种字符流（网络IO，文件IO，或则内存的字符串）
	 */
	private BufferedReader reader;

	private Map<Class<?>, List<Notifier>> notifiers;

	private List<DelinesBusEntity<? extends IDelinesEntity>> busEntities;

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

	public DelinesDocument registerDelinesEntity(Class<? extends IDelinesEntity> clazz) {
		if (clazz == null) {
			throw new RuntimeException("could not register deliens entity with NULL");
		}
		if (busEntities == null) {
			busEntities = new ArrayList<>();
		}
		for (DelinesBusEntity<?> entity : busEntities) {
			if (entity.getClazz().equals(clazz)) {
				throw new RuntimeException("Repeated registration");
			}
		}
		busEntities.add(DelinesEntityParser.parse(clazz));
		return this;
	}

	public <T extends IDelinesEntity> DelinesDocument notifyFounder(Class<T> clazz, Notifier notifier) {
		if (notifier == null) {
			return this;
		}
		if (notifiers == null) {
			notifiers = new HashMap<>();
		}
		List<Notifier> clazzNotifiers = notifiers.get(clazz);
		if (clazzNotifiers == null) {
			clazzNotifiers = new ArrayList<>();
		}
		clazzNotifiers.add(notifier);
		notifiers.put(clazz, clazzNotifiers);
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
				if (value != null) {
					boolean addToBus = true;
					List<Notifier> clazzNotifiers = notifiers.get(bus.getClazz());
					if (clazzNotifiers != null) {
						for (Notifier clazzNotifier : clazzNotifiers) {
							addToBus = addToBus && clazzNotifier.live(bus, value);
						}
					}
					if (addToBus) {
						bus.add(value);
					}
				}
			}
			index++;
		}
		return this;
	}

	public <T extends IDelinesEntity> DelinesBusEntity<T> getBusEntity(Class<T> clazz){
		for (DelinesBusEntity<? extends IDelinesEntity> busEntity : busEntities) {
			if (busEntity.getClazz().equals(clazz)) {
				return (DelinesBusEntity<T>) busEntity;
			}
		}
		return null;
	}

	public <T extends IDelinesEntity> List<T> getFoundEntities(Class<T> clazz) {
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
