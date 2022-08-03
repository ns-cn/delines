package cn.tangyujun.delines.handler;

import cn.tangyujun.delines.DelinesBusEntity;
import cn.tangyujun.delines.IDelinesEntity;

@FunctionalInterface
public interface Notifier<T extends IDelinesEntity> {

	boolean notify(DelinesBusEntity<T> bus, T entity);
}
