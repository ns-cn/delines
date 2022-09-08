package com.tangyujun.delines.handler;

import com.tangyujun.delines.DelinesBusEntity;

@FunctionalInterface
public interface Notifier<T> {

	boolean notify(DelinesBusEntity<T> bus, T entity);
}
