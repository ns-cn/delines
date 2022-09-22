package com.tangyujun.delines.annotation;

import java.lang.annotation.*;

/**
 * 实体生成器
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityCreator {
}