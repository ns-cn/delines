package cn.tangyujun.delines.handler;

import cn.tangyujun.delines.DelinesBusEntity;
import cn.tangyujun.delines.IDelinesEntity;

@FunctionalInterface
public interface Notifier {

	boolean live(DelinesBusEntity<? extends IDelinesEntity> bus, IDelinesEntity entity);
}
