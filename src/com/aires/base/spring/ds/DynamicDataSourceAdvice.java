package com.aires.base.spring.ds;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
/**
 * 支持动态数据源
 * @author User
 *
 */
public class DynamicDataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice{

	@Override
	public void afterReturning(Object returnValue, Method m, Object[] args,
			Object target) throws Throwable {
		DynamicDataSource.remove();		
	}

	@Override
	public void before(Method m, Object[] args, Object target)
			throws Throwable {
		//当有直接调用set方法时，以set调用为准
		if(DynamicDataSource.get() == null){
			DS ds = m.getAnnotation(DS.class);

			if(ds == null)ds = target.getClass().getAnnotation(DS.class);
			
			if(ds != null)DynamicDataSource.set(ds.value());
		}
	}

}
