package com.aires.base.dao.orm.hibernate;

import java.util.List;

import com.aires.base.dao.IOrmDao;
import com.aires.base.dao.ISqlDao;

public interface IHibernateDao<T> extends IOrmDao<T>, ISqlDao{
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
