package com.mai.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mai on 16/6/30.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface DataBase {
    /**
     * 数据库名称
     * @return
     */
    String name();

    /**
     * 多数据库时，做区分
     * @return
     */
    int key() default 0;

    /**
     * 数据库版本
     * @return
     */
    int version() default 1;
}
