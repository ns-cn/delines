package com.tangyujun.delines.handler;

import com.tangyujun.delines.DelinesBusEntity;

/**
 * 注册提示信息
 *
 * @param <T> 具体类型
 */
@FunctionalInterface
public interface Notifier<T> {

	/**
	 * 提示
	 *
	 * @param bus    解析后的实体类型
	 * @param entity 对应的实体
	 * @return 是否添加的到解析成功的list中
	 */
	boolean notify(DelinesBusEntity<T> bus, T entity);
}
