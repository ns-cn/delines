package com.tangyujun.delines.handler;

import com.tangyujun.delines.DelinesBusEntity;
import com.tangyujun.delines.IDelinesEntity;

@FunctionalInterface
public interface Notifier<T extends IDelinesEntity> {

	boolean notify(DelinesBusEntity<T> bus, T entity);
}
