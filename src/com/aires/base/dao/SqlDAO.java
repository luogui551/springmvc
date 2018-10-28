package com.aires.base.dao;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author aires
 * 2017年1月16日 下午4:34:52
 * 描述：支持SQL操作的接口
 */
public interface SqlDAO extends DAO{
	/**
	 * 执行更新SQL语句
	 * @param sql
	 * @param param 参数
	 * @return 影响记录数
	 */
	public int sqlUpdate(String sql, Object...params);
	/**
	 * 批量更新
	 * @param sql
	 * @param params 参数
	 * @return 影响记录数数组（受数据库驱动影响，该数据可能不准确）
	 */
	public int[]sqlBatchUpdate(String sql, List<Object[]>params);
	/**
	 * 执行SQL查询
	 * @param sql
	 * @param params
	 * @return 多个字段时返回Object[]数组List, 单个字段返回Object(非数组)对象List
	 */
	public <K>List<K>sqlList(String sql, Object...params);
	/**
	 * 执行SQL查询，返回指定页的结果
	 * @param start 记录开始位置
	 * @param limit 最大记录数
	 * @param sql
	 * @param params
	 * @return 多个字段时返回Object[]数组List, 单个字段返回Object(非数组)对象List
	 */
	public <K>List<K>sqlList(int start, int limit, String sql, Object...params);
	/**
	 * 执行SQL查询,并转换为对象
	 * @param clz 实体对应的class对象
	 * @param sql
	 * @param params
	 * @return 对应实体列表
	 */
	public <K> List<K>sqlList(Class<K> clz, String sql, Object...params);
	/**
	 * 执行SQL查询,并转换为对象(查询指定列并使用别名映射实体属性)
	 * @param fieldNames 与查询返回列一一对应的实体属性名
	 * @param clz
	 * @param sql
	 * @param params
	 * @return
	 */
	public <K> List<K>sqlList(String[]fieldNames, Class<K> clz, String sql, Object...params);
	/**
	 * 执行SQL查询,并转换为对象,带分页
	 * @param start 结果集开始序号，从0开始
	 * @param limit 结果集数量
	 * @param clz 结果集需要转换为的对象Class
	 * @param sql
	 * @param params
	 * @return
	 */
	public <K> List<K>sqlList(int start, int limit, Class<K> clz, String sql, Object...params);
	/**
	 * 执行SQL查询,并转换为对象,带分页
	 * @param start 结果集开始序号，从0开始
	 * @param limit 结果集数量
	 * @param fieldNames 与查询返回列一一对应的实体属性名
	 * @param clz 结果集需要转换为的对象Class
	 * @param sql
	 * @param params
	 * @return
	 */
	public <K> List<K>sqlList(int start, int limit, String[]fieldNames, Class<K> clz, String sql, Object...params);
	/**
	 * 执行SQL查询,并转换为map
	 * @param columns 当columns == null时,使用数据库字段名大写形式
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public List<Map<String, Object>>sqlListOfMap(String[]columns, String sql, Object...params);
	/**
	 * 执行SQL查询，返回指定页的结果
	 * @param start 首条记录序号
	 * @param limit 最大记录数
	 * @param columns 当columns == null时,使用数据库字段名大写形式
	 * @param sql 需要执行查询的SQL
	 * @param params 执行SQL需要的参数
	 * @return
	 */
	public List<Map<String, Object>>sqlListOfMap(int start, int limit, String[]columns, String sql, Object...params);
}
