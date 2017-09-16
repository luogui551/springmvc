package com.aires.base.spring.ds;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.aires.base.spring.util.SpringUtils;
/**
 * 多数据源支持
 * @author User
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource{
	
	public static final String DEFAULT_DS_NAME = "dataSource";

	private static final ThreadLocal<String>dsKey = new ThreadLocal<String>();
	
	private Map<Object, Object>targetDataSources;
	
	private DataSource resolvedDefaultDataSource;
	
	public DynamicDataSource(){
		targetDataSources = new HashMap<Object, Object>();
		super.setTargetDataSources(targetDataSources);//初始化时必须有值
	}
	/**
	 * 获取当前实例
	 * @return
	 */
	public static DynamicDataSource getInstance(){
		return SpringUtils.getBean(DynamicDataSource.class);
	}
	/**
	 * 数据源注册
	 * @param name
	 * @param ds
	 */
	public void registe(String name, DataSource ds){
		targetDataSources.put(name, ds);
	}
	
	/**
	 * 移除数据源
	 * @param dsName
	 */
	public void remove(String dsName){
		targetDataSources.remove(dsName);
	}
	
	
	@Override
	protected DataSource determineTargetDataSource() {
		Object lookupKey = determineCurrentLookupKey();
		if(lookupKey == null)return resolvedDefaultDataSource;
		
		Object dataSource = this.targetDataSources.get(lookupKey);
		if (dataSource != null ) {
			return this.resolveSpecifiedDataSource(dataSource);
		}
		return resolvedDefaultDataSource;
	}
	
	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		this.targetDataSources = targetDataSources;
	}
	
	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		this.resolvedDefaultDataSource = resolveSpecifiedDataSource(defaultTargetDataSource);
	}

	/**
	 * 初始化当前context中的所有DataSource
	 */
	public void init(){
		Map<String, DataSource>beans = SpringUtils.getBeans(DataSource.class);
		for(Map.Entry<String, DataSource>e : beans.entrySet()){
			if(!(e.getValue() instanceof DynamicDataSource)){
				if(DEFAULT_DS_NAME.equals(e.getKey())){
					setDefaultTargetDataSource(e.getValue());
				}
				targetDataSources.put(e.getKey(), e.getValue());
			}
		}
	}
	
	/**
	 * 设置数据源类型
	 * @param key
	 */
	public static void set(String key){
		dsKey.set(key);
	}
	
	/**
	 * 清除
	 */
	public static void remove(){
		dsKey.remove();
	}
	/**
	 * 获取当前值
	 * @return
	 */
	public static String get(){
		return dsKey.get();
	}
	
	@Override
	protected Object determineCurrentLookupKey() {
		return dsKey.get();
	}
}
