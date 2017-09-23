package com.aires.base.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.aires.base.page.PagedList;

/**
 * @author aires
 * 2017年9月21日 下午4:50:40
 * 描述：支持实例-关系表映射操作接口
 */
public interface OrmService<T> {
	/**
	 * 保存实例
	 * @param entity
	 * @return 实体的id
	 */
	public <K>Serializable save(K entity);
	/**
	 * 批量保存
	 * @param list
	 */
	public <K>void save(List<K> list);	
	/**
	 * 批量保存或更新
	 * @param list
	 */
	public <K>void saveOrUpdate(List<K> list);
	/**
	 * 删除
	 * @param id 以英文逗号(,)分隔表示多个
	 */
	public boolean delete(String id);
	/**
	 * 批量删除
	 * @param key
	 */
	public boolean delete(String[]ids);
	/**
	 * 删除实体
	 * @param persistentEntity 持久化实体对像
	 * @return
	 */
	public <K>void delete(K persistentEntity);
	/***
	 * 更新记录
	 * @param po
	 */
	public <K>void update(K entity);
	/**
	 * 更新对象中的指定字段
	 * @param t
	 * @param fieldNames 要更新的字段
	 */
	public <K>void update(K entity, Collection<String>fieldNames);
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public T queryById(String id);
	/**
	 *查询记录
	 * @param clazz
	 * @return
	 */
	public List<T>list();
	/**
	 * 查询记录
	 * @param orderBy 排序字段
	 * @return
	 */
	public List<T>listWithOrder(String orderBy);
	/**
	 * 带分页查询
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PagedList<T> query(int pageNum, int pageSize);
	/**
	 * 带分页查询
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param orderBy 排序字段
	 * @return
	 */
	public PagedList<T> query(int pageNum, int pageSize, String orderBy);
	/**
	 * 带分页查询
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param parametersMap 查询条件
	 * @return 可能为PagedList<T>或者List<T>(pageNum = -1 && pageSize = -1)
	 */
	public Object query(int pageNum, int pageSize, Map<String, String>parameters);
	/**
	 * 根据条件查询单个对象,以等于(=)作为运算符
	 * @param parameters
	 * @return 
	 * @exception 结果不是唯一或没有结果时，抛出UnexpectedException
	 */
	public T one(Map<String, Object>parameters);
}
