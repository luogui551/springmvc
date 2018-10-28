package com.aires.hibernate.dao;

import java.util.List;

import com.aires.base.dao.BaseDAO;
/**
 * 
 * @author aires
 * 2017年9月23日 下午5:30:46
 * 描述：hibernate的dao接口
 */
public interface HibernateDAO extends BaseDAO{
	/**
	 * 执行更新HQL语句
	 * @param hql HQL语句
	 * @param obj 参数
	 */
	public int update(String hql, Object...params);
	/**
	 * 执行查询HQL语句
	 * @param hql HQL语句
	 * @param param 参数
	 * @return
	 */
	public <K>List<K> query(String hql, Object...params);
	/**
	 * 执行HQL语句查询,返回指定页的结果
	 * @param hql
	 * @param start 记录开始位置
	 * @param limit 最大记录数
	 * @param param
	 * @return
	 */
	public <K>List<K> query(String hql, int start, int limit, Object...params);
}
