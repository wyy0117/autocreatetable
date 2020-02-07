package com.wyy.actable.annotation;

import java.lang.annotation.*;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Index {
	
	/**
	 * 索引的名字，不设置默认为{idx_当前标记字段名@Column的name}
	 * @return
	 */
	public String name() default "";

}

