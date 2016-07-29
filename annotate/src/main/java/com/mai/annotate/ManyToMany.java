package com.mai.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mai on 16/7/9.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface ManyToMany {

    /**
     * 对面类类型全称
     *
     * @return
     */
    String type();

    /**
     * 设置是否维护多对多关系
     * 多对多只需要一方维护关系
     *
     * @return
     */
    boolean inverse();

    /**
     * 是否开启懒加载
     *
     * @return
     */
    boolean lazy() default true;

    /**
     * 级联，包括insert(插入)、insertOrReplace(插入或者替换)、update(更新)、delete(删除)
     * @return
     */
    Cascade[] cascade() default {};
}
