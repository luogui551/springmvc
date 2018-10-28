package com.aires.base.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.aires.base.bean.LogicBean;
import com.aires.base.dao.OrmDAO;
import com.aires.base.service.OrmService;

/**
 * @author aires
 * 2017年9月21日 下午4:52:25
 * 描述：
 */
public abstract class OrmServiceImpl<T, X extends OrmDAO> extends BaseServiceImpl<X> implements OrmService<T>{
	
	@Override
	public <K>Serializable save(K entity){
		checkNull(entity);
		
		return this.getDao().save(entity);
	}
	
	@Override
	public <K>void save(List<K> list){
		checkNull(list);
		if(list.size() == 0)return;
		
		this.getDao().save(list);
	}
	
	@Override
	public <K>void saveOrUpdate(List<K> list){
		checkNull(list);
		if(list.size() == 0)return;
		
		this.getDao().saveOrUpdate(list);
	}
	
	@Override
	public <K> void delete(K persistentEntity) {
		getDao().del(persistentEntity);
	}
	
	@Override
	public boolean delete(String id){
		checkNull(id);
		
		if(id.indexOf(",") != -1){//调用批量接口
			return this.getDao().del(getGenericClass(), id.split(",")) > 0;
		}
		return this.getDao().del(getGenericClass(), id) > 0;
	}
	
	@Override
	public boolean delete(String[]ids){
		checkNull(ids);
		
		return this.getDao().del(getGenericClass(), ids) > 0;
	}
	
	@Override
	public <K>void update(K entity){
		this.getDao().update(entity);
	}
	
	@Override
	public T queryById(String id){
		checkNull(id);
		
		return this.getDao().queryById(getGenericClass(), id);
	}
	
	@Override
	public List<T>list(){
		return this.listWithOrder(null);
	}
//-------------------------------------------------------------------------------	
	
	protected boolean isLogicBean(){
		return LogicBean.class.isAssignableFrom(getGenericClass());
	}
	
	private Class<T>genericClass;
	
	/**
	 * 获取泛型类的Class
	 * @return
	 */
	@SuppressWarnings("unchecked")
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
