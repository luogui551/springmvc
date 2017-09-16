/**
 * 
 */
package com.aires.base.service;

import java.util.Map;

import com.aires.base.page.PagedList;

/**
 * @author aires
 * 2016-9-24 下午1:37:58
 * 描述：
 */
public interface ISqlService {
	/**
	 * 执行SQL查询,并转换为对象,带分页
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param clz 实体Class
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public <K> PagedList<K>sqlQuery(int pageNum, int pageSize, Class<K> clz, String sql, Object...params);
	/**
	 * 执行SQL查询,并转换为对象,带分页
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param fieldNames 与查询返回列一一对应的实体属性名,当columns == null时使用默认格式
	 * @param clz 实体Class
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public <K> PagedList<K>sqlQuery(int pageNum, int pageSize, String[]fieldNames, Class<K> clz, String sql, Object...params);
	/**
	 * 执行SQL查询，返回Map形式的结果,带分页
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public PagedList<Map<String, Object>>sqlQueryOfMap(int pageNum, int pageSize, String sql, Object...params);	
	/**
	 * 执行SQL查询，返回Map形式的结果,带分页
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param columns map的key序列，需要与查询返回列一一对应,当columns == null时使用列名.toUpperCase()
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public PagedList<Map<String, Object>>sqlQueryOfMap(int pageNum, int pageSize, String[]columns, String sql, Object...params);
}
