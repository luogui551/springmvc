package com.aires.sql.dao;
/**
 * 
 * @author aires
 * 2017年1月16日 下午5:58:55
 * 描述：支持Oracle
 */
public class OracleSpringSqlDAO extends SpringSqlDAO{

	@Override
	protected PagedData getPagedSql0(String sql, int start, int limit, Object[]params) {
		return new PagedData("select * from (select t1.*,t1.rownum rn from (" + sql + ") t1) t2 where t2.rn > ? and t2.rn <= ?", params, start, start + limit);
	}

}
