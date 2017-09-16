package com.aires.base.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * 
 * @author aires
 * 2017年1月17日 上午11:56:52
 * 描述：继承本类后可获取泛型类型
 */
@SuppressWarnings("unchecked")
public class GenericSupport<T> {
	
	private Class<T>genericClass;
	
	/**
	 * 获取泛型类的Class
	 * @return
	 */
	public Class<T> getGenericClass(){
		if(this.genericClass == null){
			Type genType = this.getClass().getGenericSuperclass();  
			  
	        Type[] params = ((ParameterizedType) genType).getActualTypeArguments(); 
	        genericClass = (Class<T>) params[0];
		}
        return this.genericClass;
	}
	
	/**
	 * 获取泛型类的名称
	 * @return
	 */
	public String getGenericClassName(){
		return this.getGenericClass().getName();
	}
}
