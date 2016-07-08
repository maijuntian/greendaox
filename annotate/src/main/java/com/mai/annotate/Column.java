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
public @interface Column {

    /**
     * 列名
     * @return
     */
    String name() default "";

    /**
     * 是否可以为空
     * @return
     */
    boolean isNull() default true;

    /**
     * 是否唯一
     * @return
     */
    boolean unique() default false;
}
