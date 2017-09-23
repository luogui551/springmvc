package com.aires.sql.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.aires.base.dao.SqlDAO;
/**
 * 
 * @author aires
 * 2017年1月16日 下午4:40:53
 * 描述：基于spring的JdbcTemplate的实现
 */
public abstract class SpringSqlDAO extends JdbcTemplate implements SqlDAO{
	
	@Override
	@Resource
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	public int sqlUpdate(String sql, Object... params) {
		return super.update(sql, params);
	}

	@Override
	public int[] sqlBatchUpdate(String sql, List<Object[]> params) {
		return super.batchUpdate(sql, params);
	}
	
	@Override
	public <K>List<K> sqlList(String sql, Object... params) {
		return sqlList(-1, -1, sql, params);
	}

	@Override
	public <K>List<K> sqlList(int start, int limit, String sql, Object... params) {
		PagedData data = getPagedSql(sql, start, limit, params);
		return super.query(data.sql, data.params, new AbsRowMapper<K>(false, null){

			@SuppressWarnings("unchecked")
			@Override
			public K mapRow0(ResultSet rs) throws SQLException {
				if(columnCount == 1){
					return(K) JdbcUtils.getResultSetValue(rs, 1);
				} else {
					Object[]v = new Object[columnCount];
					for(int i = 1; i <= columnCount; i++){
						v[i - 1] = JdbcUtils.getResultSetValue(rs, i);
					}
					return (K) v;
				}
			}
			
		});
	}

	@Override
	public <K> List<K> sqlList(Class<K> clz, String sql, Object... params) {
		
		return sqlList(-1, -1, null, clz, sql, params);
	}

	@Override
	public <K> List<K> sqlList(String[] fieldNames, Class<K> clz, String sql, 
			Object... params) {
		
		return sqlList(-1, -1, fieldNames, clz, sql, params);
	}

	@Override
	public <K> List<K> sqlList(int start, int limit, Class<K> clz, String sql, 
			Object... params) {
		
		return sqlList(start, limit, null, clz, sql, params);
	}

	@Override
	public <K> List<K> sqlList(int start, int limit, String[] fieldNames, Class<K> clz, String sql, Object... params) {
		PagedData data = getPagedSql(sql, start, limit, params);
		return super.query(data.sql, data.params, getBeanRowMapper(fieldNames, clz));
	}

	@Override
	public List<Map<String, Object>> sqlListOfMap(String[] columns, String sql,
			Object... params) {
		return sqlListOfMap(-1, -1, columns, sql, params);
	}

	@Override
	public List<Map<String, Object>> sqlListOfMap(int start, int limit, String[] columns, String sql, Object... params) {
		PagedData data = getPagedSql(sql, start, limit, params);
		return super.query(data.sql, data.params, getColumnMapRowMapper(columns));
	}
	
//-------------------------------------------------分隔线---------------------------------------------------------
	
	private abstract class AbsRowMapper<K> implements RowMapper<K>{
		/**
		 * 列名
		 */
		protected String[]names;
		/**
		 * 结果集的列数
		 */
		protected int columnCount;
		/**
		 * 是否将数据库字段名按默认规则转变为实体bean字段名
		 */
		private boolean transferFieldName;
		/**
		 * 别名，临时使用
		 */
		private String[]aliases;
		
		public AbsRowMapper(boolean transferFieldName, String[]aliases){
			this.transferFieldName = transferFieldName;
			this.aliases = aliases;
		}
		@Override
		public K mapRow(ResultSet rs, int rowNum) throws SQLException {
			if(names == null){//同一个对象仅初始化一次
				ResultSetMetaData rsmd = rs.getMetaData();
				columnCount = rsmd.getColumnCount();
				if(aliases == null){
					names = new String[columnCount];
					for(int i = 0; i < columnCount; i++)names[i] = transferFieldName ? defaultFieldName(rsmd.getColumnName(i + 1)) : rsmd.getColumnName(i + 1);
				}else{
					names = aliases;
					aliases = null;
				}
			}
			K k = mapRow0(rs);
			return k;
		}	
		/**
		 * 从结果集中获取一行数据
		 * @param rs
		 * @return
		 * @throws SQLException
		 */
		protected abstract K mapRow0(ResultSet rs) throws SQLException ;
		/**
		 * 默认转换规则：除下划线后一个字母大写外，其它全部小写
		 * @param columnName 待转数据库字段名
		 * @return
		 */
		protected String defaultFieldName(String columnName){
			StringBuffer result = new StringBuffer();
			Matcher m = beanNamePattern.matcher(columnName.toLowerCase());
			while(m.find()){
				m.appendReplacement(result, m.group().toUpperCase());
			}
			m.appendTail(result);
			return result.toString();
		}
	}
	private static final Pattern beanNamePattern = Pattern.compile("_(.)");
	
	//结果集转换为实体，以fieldNames名作为实体属性名映射（若clz实现RowMapper，则返回该Class实例）
	@SuppressWarnings("unchecked")
	private <K>RowMapper<K>getBeanRowMapper(final String[] fieldNames, final Class<K> clz){
		if(RowMapper.class.isAssignableFrom(clz)){
			try {
				return (RowMapper<K>) clz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return new AbsRowMapper<K>(true, fieldNames){
			
			@Override
			public K mapRow0(ResultSet rs) throws SQLException {
				K k;
				try {
					k = (K) clz.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				for(int i = 0; i < columnCount; i++){
					try {
						PropertyUtils.setProperty(k, names[i], JdbcUtils.getResultSetValue(rs, i + 1));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				return k;
			}			
		};
	}
	//结果集转换为map,以columns指定名为key
	private RowMapper<Map<String, Object>>getColumnMapRowMapper(final String[] columns){
		return new AbsRowMapper<Map<String, Object>>(false, columns){

			@Override
			public Map<String, Object> mapRow0(ResultSet rs) throws SQLException {
				Map<String, Object>row = new HashMap<String, Object>();
				for(int i = 1; i < columnCount; i++){
					row.put(names[i], JdbcUtils.getResultSetValue(rs, i + 1));
				}
				return row;
			}			
		};
	}
	/**
	 * 
	 * @author aires
	 * 2017年2月13日 上午10:53:18
	 * 描述：用于分页时内部参数传递使用
	 */
	protected class PagedData {
		private String sql;
		private Object[]params;
		/**
		 * @param sql 无分页内容的SQL
		 * @param params SQL执行需要的参数
		 * @param pageParams 分页信息
		 */
		public PagedData(String sql, Object[]params, int...pageParams){
			this.sql = sql;
			this.params = params;
			if(pageParams.length > 0){
				int len = params.length;
				Object[]tmp = Arrays.copyOf(params, len + pageParams.length);
				System.arraycopy(pageParams, 0, tmp, len, pageParams.length);

				this.params = tmp;
			}
		}
	}
	//获取分页信息
	private PagedData getPagedSql(String sql, int start, int limit, Object[]params){
		if(start < 0 || limit <= 0){
			if(start == -1 && limit == -1)//内部特殊处理，表示不分页
				return new PagedData(sql, params);
			else
				throw new IllegalArgumentException("分页参数不能为负数：start:" + start + ",limit:" + limit);
		}
		return getPagedSql0(sql, start, limit, params);
	}
	/**
	 * 不同数据库有不同的形式
	 * @param sql
	 * @param start
	 * @param limit
	 * @return
	 */
	protected abstract PagedData getPagedSql0(String sql, int start, int limit, Object[]params);
}
