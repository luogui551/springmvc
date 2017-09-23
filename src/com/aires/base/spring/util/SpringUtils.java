package com.aires.base.spring.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 
 * @author aires
 * 2016-10-30 下午5:13:41
 * 描述：工具类，用于获取容器中的实例,资源等 
 */
@Component
public class SpringUtils implements ApplicationContextAware{
	
	private static LinkedList<ApplicationContext> contexts = new LinkedList<ApplicationContext>();
	
	/**
	 * 设置Spring上下文
	 * @param ctx
	 */
	public static void setWebApplicationContext(ApplicationContext ctx){
		contexts.addFirst(ctx);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		contexts.addFirst(ctx);
	}
	
	/**
	 * 注意方法使用前提为当前类必须被Spring容器初始化
	 * @param beanName
	 * @return
	 */
	public static<T>T getBean(String beanName) {
		return getBean0(beanName);
	}
	/**
	 * 
	 * @param clz
	 * @return
	 */
	public static<T>T getBean(Class<T>clz) {
		return getBean0(clz);
	}
	/**
	 * 获取指定类型的所有实例
	 * @param clz
	 * @return
	 */
	public static<T> Map<String, T> getBeans(Class<T>clz) {
		Map<String, T>result = new HashMap<String, T>(3);
		Iterator<ApplicationContext>it = contexts.iterator();
		while(it.hasNext()){
			ApplicationContext ctx = it.next();
			try{
				Map<String, T>tmp = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, clz);
				if(tmp.size() > 0){
					result.putAll(tmp);
				}
			}catch(Exception e){
			}
		}
		return result;
	}
	
	private static<T>T getBean0(Object arg){
		T o = null;
		Iterator<ApplicationContext>it = contexts.iterator();
		while(it.hasNext()){
			ApplicationContext ctx = it.next();
			try{
				if(arg instanceof String){
					o = (T) ctx.getBean((String)arg);
				}else{
					o = ctx.getBean((Class<T>)arg);
				}
				break;
			}catch(Exception e){
			}
		}
		return o;
	}
	
}
