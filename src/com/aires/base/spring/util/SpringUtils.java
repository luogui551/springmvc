package com.aires.base.spring.util;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
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
	/**
	 * 获取当前bundle(仅限UAP工程)
	 * @return
	 */
	public static Bundle currentBundle(){
		return getBundle(Thread.currentThread().getContextClassLoader());
	}
	/**
	 * 获取指定ClassLoader对应的bundle(仅限UAP工程)
	 * @param cl
	 * @return
	 */
	public static Bundle getBundle(ClassLoader cl){
		
		if(cl instanceof BundleReference)return ((BundleReference)cl).getBundle();
		//ModuleDelegatingClassLoader没有导出
		Field f;
		try {
			f = cl.getClass().getDeclaredField("backingBundle");
			f.setAccessible(true);
			
			return (Bundle) f.get(cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取指定Class对应的bundle
	 * @param classFromBundle
	 * @return
	 */
	public static Bundle getBundle(Class<?> classFromBundle){
		return getBundle(classFromBundle.getClassLoader());
	}
	/**
	 * 获取指定资源
	 * @param path
	 * @return
	 */
	public static URL getResource(String path){
		return currentBundle().getResource(path);
	}
	/**
	 * 获取指定目录下的所有资源
	 * @param basePath 基于bundle根目录的目录
	 * @return
	 */
	public static Enumeration<URL> getResources(String basePath){
		return getResources(basePath, "");
	}
	/**
	 * 获取bundle下指定资源
	 * @param basePath 基于bundle根目录的目录
	 * @param pattern 资源模式，如*.jpg表示所有jpg文件
	 * @return
	 */
	public static Enumeration<URL> getResources(String basePath, String pattern){
		return getResources(basePath, pattern, true);
	}
	/**
	 * 获取bundle下指定资源
	 * @param basePath 基于bundle根目录的目录
	 * @param pattern 资源模式，如*.jpg表示所有jpg文件
	 * @param recurse 是否递归获取
	 * @return
	 */
	public static Enumeration<URL> getResources(String basePath, String pattern, boolean recurse){
		return currentBundle().findEntries(basePath, pattern, recurse);
	}
	/**
	 * 获取当前bundle下指定路径的资源(UAP, base-springmvc为非jar方式部署)
	 * @param path 相对bundle根目录的资源
	 * @return File
	 */
	public static File getFileResource(String path){
		return getFileResource(path, SpringUtils.class);	
	}
	/**
	 * 获取当前bundle下指定路径的资源(UAP, 该bundle为非jar方式部署)
	 * @param path 
	 * @param classFromBundle 当前bundle的任意class对象
	 * @return
	 */
	public static File getFileResource(String path, Class<?>classFromBundle){
		return new File(getBasePath(classFromBundle) + currentBundle().getBundleId() + "/1/bundlefile/" + (path.startsWith("/") ? path.substring(1) : path));	
	}
	
	//需要本类没有打成Jar才生效
	private static String getBasePath(Class<?>clz){
		String base = clz.getProtectionDomain().getCodeSource().getLocation().getPath();
		String key = "/bundles/";
		return base.substring(0, base.lastIndexOf(key) + key.length());
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
