package com.mai.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mai on 16/6/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Table {

    /**
     * 表名
     *
     * @return
     */
    String name() default "";

    /**
     * 多数据库时，做区分
     * @return
     */
    int dbKey() default 0;

    /**
     * 创建索引
     *
     * @return
     */
    String[] createIndex() default {};
}
