package com.mai.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mai on 16/6/29.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Id {
    /**
     * 主键名
     * @return
     */
    String name() default "";

    /**
     * 是否自增
     * @return
     */
    boolean autoIncrement() default false;
}
