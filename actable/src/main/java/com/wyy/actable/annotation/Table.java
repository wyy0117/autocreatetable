package com.wyy.actable.annotation;

import java.lang.annotation.*;


/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {

	/**
	 * 表名
	 * @return 表名
	 */
	public String name();
}
