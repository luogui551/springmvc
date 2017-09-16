package com.aires.base.service;

import java.util.List;
import java.util.Map;

import com.aires.base.dao.ISqlDao;
import com.aires.base.page.PagedList;

public class SqlService implements ISqlService{
	
	private ISqlDao dao;

	@Override
	public <K> PagedList<K> sqlQuery(int pageNum, int pageSize, Class<K> clz, String sql, Object... params) {
		return sqlQuery(pageNum, pageSize, null, clz, sql, params);
	}

	@Override
	public <K> PagedList<K> sqlQuery(int pageNum, int pageSize, String[] fieldNames, Class<K> clz, String sql,
			Object... params) {
		PagedList<K>result = new PagedList<K>(pageNum, pageSize);
		List<K>data = dao.sqlList(result.getStart(), pageSize, fieldNames, clz, sql, params);
		result.setRows(data);
		String countSql = "select count(*) from (" + sql + ") tmp";
		Number num = dao.sqlOne(countSql, params);
		result.setTotalCount(num.intValue());
		return result;
	}
	
	@Override
	public PagedList<Map<String, Object>> sqlQueryOfMap(int pageNum, int pageSize, String sql, Object... params) {

		return sqlQueryOfMap(pageNum, pageSize, null, sql, params);
	}

	@Override
	public PagedList<Map<String, Object>> sqlQueryOfMap(int pageNum, int pageSize, String[] columns, String sql,
			Object... params) {
		PagedList<Map<String, Object>>result = new PagedList<Map<String, Object>>(pageNum, pageSize);
		List<Map<String, Object>>data = dao.sqlListOfMap(result.getStart(), pageSize, columns, sql, params);
		result.setRows(data);
		String countSql = "select count(*) from (" + sql + ") tmp";
		Number num = dao.sqlOne(countSql, params);
		result.setTotalCount(num.intValue());
		return result;
	}

}
