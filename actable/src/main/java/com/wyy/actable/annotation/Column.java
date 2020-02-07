package com.wyy.actable.annotation;

import java.lang.annotation.*;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    /**
     * 字段名
     *
     * @return 字段名
     */
    String name();

    /**
     * 字段类型
     *
     * @return 字段类型
     */
    String type();

    /**
     * 字段长度，默认是-1
     *
     * @return 字段长度，默认是-1，必须要手动设置
     */
    int length() default -1;

    /**
     * 小数点长度，默认是-1
     *
     * @return 小数点长度，默认是-1,必须要手动设置
     */
    int decimal() default -1;

    /**
     * 是否为可以为null，true是可以，false是不可以，默认为true
     *
     * @return 是否为可以为null，true是可以，false是不可以，默认为true
     */
    boolean nullable() default true;

    /**
     * 是否是主键，默认false
     *
     * @return 是否是主键，默认false
     */
    boolean key() default false;

    /**
     * 是否自动递增，默认false 只有主键才能使用
     *
     * @return 是否自动递增，默认false 只有主键才能使用
     */
    boolean autoIncrement() default false;

    /**
     * 默认值，默认为null
     *
     * @return 默认值，默认为null
     */
    String defaultValue() default "NULL";

    /**
     * 数据表字段备注
     *
     * @return 默认值，默认为空
     */
    String comment() default "";

}
