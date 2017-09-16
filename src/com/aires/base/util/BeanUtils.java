/**
 * 
 */
package com.aires.base.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author aires
 * 2016-9-28 上午12:03:17
 * 描述：实体工具类
 */
@SuppressWarnings("unchecked")
public class BeanUtils {
	/**
	 * 获取bean指定属性的值
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static <T>T getPropertyValue(Object bean, String propertyName) {
		try {
			return (T) PropertyUtils.getProperty(bean, propertyName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 执行指定名称的无参方法
	 * @param bean
	 * @param methodName
	 * @return
	 */
	public static <T>T invoke(Object bean, String methodName){
		Method m = null;
		Class<?>clz = bean.getClass();
		do {
			try {
				m = clz.getDeclaredMethod(methodName);
				m.setAccessible(true);
			} catch (Exception e) {
				clz = clz.getSuperclass();
			}
		}while(m == null && clz != null);
		if(m == null)throw new RuntimeException("不存在方法：" + bean.getClass().getName() + "." + methodName);
		try {
			return (T) m.invoke(bean);
		} catch (Exception e) {
			throw new RuntimeException("执行" + clz.getName() + "." + methodName + "方法出错", e instanceof InvocationTargetException ? e.getCause() : e);
		}
	}
	
}
