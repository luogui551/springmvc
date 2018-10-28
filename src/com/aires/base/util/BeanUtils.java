/**
 * 
 */
package com.aires.base.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author aires
 * 2016-9-28 上午12:03:17
 * 描述：
 */
public class BeanUtils {
	/**
	 * 获取bean指定属性的值
	 * @param bean
	 * @param propertyName
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static <T>T getPropertyValue(Object bean, String propertyName) throws NoSuchMethodException{
		try {
			return (T) PropertyUtils.getProperty(bean, propertyName);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
