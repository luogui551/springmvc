package com.aires.hibernate.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aires.base.annotation.Query;
import com.aires.base.dao.SqlDAO;
import com.aires.base.page.PagedList;
import com.aires.base.service.BaseService;
import com.aires.base.service.impl.OrmServiceImpl;
import com.aires.base.util.BeanUtils;
import com.aires.hibernate.dao.SpringBasedHibernateDAO;
import com.aires.sql.service.BaseSqlService;

/**
 * @author aires
 * 2017年9月22日 下午3:01:22
 * 描述：
 */
public abstract class HibernateService<T> extends OrmServiceImpl<T, SpringBasedHibernateDAO> implements BaseService<T>{
	
	@Override
	public <K>void update(K entity, Collection<String>fieldNames){
		StringBuilder hql = new StringBuilder("update " + entity.getClass().getName() + " set");
		int index = 0;
		List<Object>params = new LinkedList<Object>();
		for(String fieldName : fieldNames){
			if("id".equals(fieldName))continue;
			try {
				Object o = BeanUtils.getPropertyValue(entity, fieldName);
				if(index++ > 0)hql.append(",");
				hql.append(" ").append(fieldName).append("=?");
				params.add(o);
			} catch (NoSuchMethodException e) {
				//没有对应属性
			}
		}
		hql.append(" where id=?");
		try {
			params.add(BeanUtils.getPropertyValue(entity, "id"));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		this.getDao().update(hql.toString(), params.toArray(new Object[params.size()]));	
	}

	@Override
	public List<T>listWithOrder(String orderBy){
		return this.list(addOrderBy(hqlForAll(), orderBy));
	}
	
	@Override
	public PagedList<T> query(int pageNum, int pageSize){
		return this.query(hqlForAll(), pageNum, pageSize);
	}
	
	@Override
	public PagedList<T> query(int pageNum, int pageSize, String orderBy){
		return this.query(addOrderBy(hqlForAll(), orderBy), pageNum, pageSize);
	}
	/**
	 * 
	 * @param hql 初始hql，格式为from className [where deleted=false]
	 * @param params 参数值,传入时数据为空
	 * @param parametersMap请求参数(从其中获取对应数据后，必须删除对应项，否则会重复拼接)
	 * @return 表示是否已添加where关键字
	 */
	protected boolean preQueryHqlBuild(StringBuilder hql, List<Object>params, Map<String, String>parameters){
		return isLogicBean();//会自动拼接where条件
	}
	
	@Override
	public Object query(int pageNum, int pageSize, Map<String, String>parameters){
		StringBuilder hql = new StringBuilder(hqlForAll());
		String orderBy = parameters.get("orderBy");
		if(orderBy != null)parameters.remove("orderBy");
		
		List<Object>params = new ArrayList<Object>(parameters.size());
		boolean hasWhere = preQueryHqlBuild(hql, params, parameters);

		String ql = buildHqlCondition(hql, params, parameters, hasWhere);
		if(pageNum == -1 && pageSize == -1){
			return this.list(addOrderBy(ql, orderBy), params.toArray(new Object[params.size()]));
		}else{
			return this.query(ql, orderBy, pageNum, pageSize, params.toArray(new Object[params.size()]));
		}
	}
	
	@Override
	public T one(Map<String, Object>parameters){
		List<Object>params = new ArrayList<Object>(parameters.size());
		return one(buildHqlCondition(new StringBuilder(hqlForAll()), params, parameters, isLogicBean()), params.toArray(new Object[params.size()]));
	}
	/**
	 * 根据hql查询单个对象
	 * @param hql
	 * @param args
	 * @return
	 * @exception 结果不是唯一或没有结果时，抛出UnexpectedException
	 */
	protected <K>K one(String hql, Object...args){
		List<K>list = this.getDao().query(hql, 0, 2, args);
		return requiredSingleResult(list);
	}
	/**
	 * 执行hql查询
	 * @param hql
	 * @param args
	 * @return
	 */
	protected <K> List<K> list(String hql, Object...args){
		return this.getDao().query(hql, args);
	}
	/**
	 * 执行hql查询，带分页
	 * @param hql
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param args
	 * @return
	 */
	protected <K>PagedList<K> query(String hql, int pageNum, int pageSize, Object...args){
		return this.query(hql, null, pageNum, pageSize, args);
	}
	/**
	 * 查询记录,添加orderBy参数用以在查询总量时不进行排序，提高效率
	 * @param hql 排序字段请作为orderBy传入
	 * @param orderBy 排序字段
	 * @param pageNum 页码
	 * @param pageSize 每页记录数
	 * @param args
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <K>PagedList<K> query(String hql, String orderBy, int pageNum, int pageSize, Object...args){
		checkPageArgs(pageNum, pageSize);
		
		PagedList page = new PagedList(pageNum, pageSize);
		page.setData(this.getDao().query(this.addOrderBy(hql, orderBy), page.getStart(), pageSize, args));
		
		if(page.getData().size() < pageSize ){
			page.setTotalCount((pageNum - 1) * pageSize + page.getData().size());
		}else{
			page.setTotalCount(this.getTotalCount(hql, args));
		}
			
		return page;
	}
	private BaseSqlService sqlService;
	/**
	 * 获取SqlService
	 * @return
	 */
	protected BaseSqlService sql(){
		if(sqlService == null){
			sqlService = new BaseSqlService(){
				@Override
				protected SqlDAO getDao() {
					return getDao();
				}
			};
		}
		return sqlService;
	}
	
	/**
	 * 通过hql获取数据总数
	 * @param hql
	 * @param args
	 * @return
	 */
	private int getTotalCount(String hql, Object...args){
		String sKey = "select", countHql = "select count(*) ";
		boolean uniqueResult = true;
		if(hql.startsWith(sKey)){
			String key = " from ";
			int index = hql.indexOf(key);
			String prev = hql.substring(0, index);
			hql = hql.substring(index);
			
			String dKey = "distinct";
			int dIndex = prev.toLowerCase().indexOf(dKey);
			if(dIndex != -1){//不支持distinct *
				String dValue = prev.substring(dIndex + dKey.length());
				hql += " group by " + dValue;
				uniqueResult = false;
			}
		}
		countHql += hql;
		if(uniqueResult){
			return intValue(countHql, args);
		}else{
			List<?> results = this.list(countHql, args);
			return results == null ? 0 : results.size();			
		}
	}
	/**
	 * 通过hql获取整数，无结果集时返回0，否则返回第一条记录数据
	 * @param hql
	 * @param params
	 * @return
	 */
	protected int intValue(String hql, Object...params){
		try {
			return ((Number)one(hql, params)).intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	//获取对象对应属性值
	/*private Object get(Object bean, String fieldName){
		try {
			return PropertyUtils.getProperty(bean, fieldName);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			return e;
		}
		return null;
	}*/
	/**
	 * 拼接条件到hql
	 * @param hql
	 * @param params
	 * @param parameters
	 * @param hasWhere
	 * @return
	 */
	private String buildHqlCondition(StringBuilder hql, List<Object>params, Map<String, ?>parameters, boolean hasWhere){
		if(parameters.size() > 0){
//			T bean;
			try {
				Class<T>clz = this.getGenericClass();
//				bean = clz.newInstance();
				
				boolean first = true;
				for(Map.Entry<String, ?>e : parameters.entrySet()){
					String fieldName = e.getKey();
					
					Method m = getGetter(clz, fieldName);
					if(m == null)continue;//属性不存在
					
					if(!hasWhere && first){
						hql.append(" where");
						first = false;
					} else {
						hql.append(" and");
					}
					Query q = m.getAnnotation(Query.class);
					if(q == null){
						hql.append(" ").append(fieldName).append("=?");
						params.add(e.getValue());
					}else{
						hql.append(" ").append(fieldName).append(" ").append(q.op());
						params.add(StringUtils.isEmpty(q.value()) ? e.getValue() : q.value().replace("{value}", String.valueOf(e.getValue())));
					}
					/*try{
						BeanUtils.getPropertyValue(bean, fieldName);
						if(!hasWhere && first){
							hql.append(" where");
							first = false;
						} else {
							hql.append(" and");
						}
						
						hql.append(" ").append(fieldName).append("=?");
						params.add(e.getValue());
					}catch(NoSuchMethodException e1){
						//属性不存在
					}*/
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return hql.toString();
	}
	/**
	 * 获取查询所有对象的hql
	 * @return
	 */
	private String hqlForAll(){
		return "from " + this.getGenericClassName() + (isLogicBean() ? (" where deleted=false") : "");
	}
}
