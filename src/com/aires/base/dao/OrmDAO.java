package com.aires.base.dao;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author aires
 * 2017年1月17日 上午11:49:55
 * 描述：支持实例-关系表映射操作接口
 */
public interface OrmDAO extends DAO{
	/**
	 * 保存对象
	 * @param entity 持久化实体对像
	 * @return
	 */
	public <K>Serializable save(K entity);
	/**
	 * 批量保存
	 * @param list 持久化实体对像列表
	 */
	public <K>void save(List<K> list);
	/**
	 * 批量保存或更新
	 * @param list 持久化实体对像列表
	 */
	public <K>void saveOrUpdate(List<K> list);
	/**
	 * 删除实体
	 * @param persistentEntity 持久化实体对像
	 * @return
	 */
	public <K>void del(K persistentEntity);
	/***
	 * 更新记录
	 * @param entity 持久化实体对像
	 */
	public <K>void update(K entity);
	/**
	 * 删除记录
	 * @param id 操作对象ID,或以逗号(,)分隔的多个ID
	 */
	public int del(Class<?>clz, String id);
	/**
	 * 批量删除记录
	 * @param ids 操作对象ID
	 */
	public int del(Class<?>clz, String[]ids);
	/**
	 * 根据ID查询单条记录
	 * @param id 数据id
	 * @return
	 */
	public <K>K queryById(Class<K>clz, String id);	
}
