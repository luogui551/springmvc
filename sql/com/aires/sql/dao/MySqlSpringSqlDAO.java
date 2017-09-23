package com.aires.sql.dao;
/**
 * 
 * @author aires
 * 2017年1月16日 下午5:58:18
 * 描述：支持MySql
 */
public class MySqlSpringSqlDAO extends SpringSqlDAO{
	
	protected PagedData getPagedSql0(String sql, int start, int limit, Object[]params){
		return new PagedData(sql += " limit ?,?", params, start, limit);
	}
}
