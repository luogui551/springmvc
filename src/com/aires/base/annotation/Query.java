/**
 * 
 */
package com.aires.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author aires
 * 2017-5-8 上午11:35:23
 * 描述：用于拼接条件(简单支持)
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Query {
	/**
	 * 值模板，需要当前值为string类型, 会用当前值替换{value}串,
	 * @return
	 */
	String value() default "";
	/**
	 * 操作符，不支持2个以上操作数的操作符(如between ? and ?)
	 * @return
	 */
	String op() default "=?";
}
