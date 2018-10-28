package com.aires.sql.service;

import java.util.List;
import java.util.Map;

import com.aires.base.dao.SqlDAO;
import com.aires.base.page.PagedList;
import com.aires.base.service.SqlService;
import com.aires.base.service.impl.BaseServiceImpl;

public abstract class BaseSqlService extends BaseServiceImpl<SqlDAO> implements SqlService{
	/**
	 * 查询单条记录
	 * @param sql
	 * @param params
	 * @return Object[]或者Object
	 */
	public <K>K sqlOne(String sql, Object...params){
		//取2条，用于判断是否唯一
		List<K>list = getDao().sqlList(0, 2, sql, params);
		return requiredSingleResult(list);
	}
	/**
	 * 执行SQL查询,并转换为对象
	 * @param clz
	 * @param sql
	 * @param params
	 * @return
	 */
	public <K>K sqlOne(Class<K> clz, String sql, Object...params){
		List<K>list = getDao().sqlList(0, 2, clz, sql, params);
		return requiredSingleResult(list);
	}
	/**
	 * 执行SQL查询,并转换为对象,带分页
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param clz 实体Class
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public <K> PagedList<K> sqlQuery(int pageNum, int pageSize, Class<K> clz, String sql, Object... params) {
		return sqlQuery(pageNum, pageSize, null, clz, sql, params);
	}
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
	public <K> PagedList<K> sqlQuery(int pageNum, int pageSize, String[] fieldNames, Class<K> clz, String sql,
			Object... params) {
		
		PagedList<K>page =build(pageNum, pageSize);
		
		sql = this.formatSql(sql, fieldNames);
		
		page.setData(getDao().sqlList(page.getStart(), pageSize, fieldNames, clz, sql, params));
		
		return setTotalCount(page, sql, pageNum, pageSize, params);		
	}
	/**
	 * 执行SQL查询，返回Map形式的结果,带分页
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public PagedList<Map<String, Object>> sqlQueryOfMap(int pageNum, int pageSize, String sql, Object... params) {

		return sqlQueryOfMap(pageNum, pageSize, null, sql, params);
	}
	/**
	 * 执行SQL查询，返回Map形式的结果,带分页
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param columns map的key序列，需要与查询返回列一一对应,当columns == null时使用列名.toUpperCase()
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public PagedList<Map<String, Object>> sqlQueryOfMap(int pageNum, int pageSize, String[] columns, String sql,
			Object... params) {
		
		return sqlQueryOfMap(pageNum, pageSize, columns, null, sql, params);
	}
	/**
	 * 添加orderBy参数用以在查询总量时不进行排序，提高效率
	 * @param sql 排序字段请作为orderBy传入
	 * @param columns map的key序列，与查询返回列一一对应,为空时使用列名.toUpperCase()
	 * @param orderBy orderBy 排序字段
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param params
	 * @return
	 */
	public PagedList<Map<String, Object>> sqlQueryOfMap(int pageNum, int pageSize, String[]columns, String orderBy, String sql, Object...params){
		
		PagedList<Map<String, Object>>page =build(pageNum, pageSize);
		
		sql = this.formatSql(sql, columns);
		
		page.setData(this.getDao().sqlListOfMap(page.getStart(), pageSize, columns, this.addOrderBy(sql, orderBy), params));
		
		return setTotalCount(page, sql, pageNum, pageSize, params);		
	}
	/**
	 * 通过sql获取整数，无结果集时返回0，否则返回第一条记录数据
	 * @param sql
	 * @param params
	 * @return
	 */
	public int sqlIntValue(String sql, Object...params){
		try {
			return ((Number)sqlOne(sql, params)).intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	/**
	 * 构建sql语句的select a,b,c内容
	 * @param sql
	 * @param columns
	 * @return
	 */
	protected String formatSql(String sql, String[]columns){
		if(columns != null && !sql.trim().toLowerCase().startsWith("select")){
			StringBuilder tmp = new StringBuilder("select ");
			for(String columnName : columns){
				tmp.append(columnName).append(",");
			}
			sql = tmp.substring(0, tmp.length() - 1) + " " + sql;
		}
		return sql;
	}
	/**
	 * 为PagedList对象设置totalCount
	 * @param page
	 * @param originalSql
	 */
	private <K>PagedList<K> setTotalCount(PagedList<K>page, String originalSql, int pageNum, int pageSize, Object[]params){
		if(page.getData().size() < pageSize ){
			page.setTotalCount((pageNum - 1) * pageSize + page.getData().size());
		}else{
			String totalCountSql = "select count(*) from (" + originalSql + ") tmp";
			page.setTotalCount(sqlIntValue(totalCountSql, params));
		}
		return page;
	}
	/**
	 * 构建对象
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	private <K>PagedList<K>build(int pageNum, int pageSize){
		checkPageArgs(pageNum, pageSize);
		
		return new PagedList<K>(pageNum, pageSize);
	}
}
