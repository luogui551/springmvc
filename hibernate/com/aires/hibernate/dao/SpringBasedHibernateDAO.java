package com.aires.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.event.EventSource;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.transform.Transformers;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.aires.base.bean.Bean;
import com.aires.base.util.BeanUtils;
/**
 * 
 * @author aires
 * 2017年1月17日 下午12:23:08
 * 描述：Hibernate实现,基于Spring HibernateTemplate
 */
@SuppressWarnings("unchecked")
public abstract class SpringBasedHibernateDAO implements HibernateDAO {
	
	private HibernateTemplate template;
	
	@Resource
	protected void setSessionFactory(SessionFactory sf){
		template = new HibernateTemplate(sf);
	}

	@Override
	public <K>Serializable save(K entity) {
		return template.save(entity);
	}

	@Override
	public <K>void save(final List<K> list) {
		if(!list.isEmpty()){
			template.execute(new HibernateCallback<K>() {
				//使用JDBC实现批量处理
				@Override
				public K doInHibernate(Session session) throws HibernateException,
						SQLException {
					Class<?>clz = list.get(0).getClass();
					
					EventSource source = (EventSource) session;
					EntityPersister persister = source.getEntityPersister( clz.getName(), clz);
					
					String idPropertyName = persister.getIdentifierPropertyName();
					List<Object[]>params = new ArrayList<Object[]>(list.size());
					for(K entity : list){
						try {
							Serializable id = BeanUtils.getPropertyValue(entity, idPropertyName);
							if(id == null)id = persister.getIdentifierGenerator().generate(source, entity);

							Object[]idValues;
							Type type = persister.getIdentifierType();
							if(type instanceof ComponentType){
							    ComponentType ct = (ComponentType) type;
								idValues = ct.getPropertyValues( id, session.getEntityMode() );
								ct.setPropertyValues(entity, idValues, session.getEntityMode() );
							}else{
								idValues = new Object[]{id};
								if(entity instanceof Bean){//TODO此处万一保存失败，原bean也会有id，可能会有问题
								    ((Bean)entity).setId(String.valueOf(id));
								}
							}
							
							Object[]tmp = persister.getPropertyValuesToInsert(entity, null, source);
							int len = tmp.length;
							tmp = Arrays.copyOf(tmp, len + idValues.length);
							System.arraycopy(idValues, 0, tmp, len, idValues.length);
							
							params.add(tmp);
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
					try {
						sqlBatchUpdate(buildInsertSQL(persister/*, clz, session*/), params);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				
			});
		}
		
	}
	
	@Override
	public <K>void saveOrUpdate(List<K> list){
		template.saveOrUpdateAll(list);
	}
	
	@Override
	public <K>void del(K persistentEntity){
		template.delete(persistentEntity);
	}

	@Override
	public int del(Class<?>clz, String id) {
		return update("delete from " + clz.getName() + " where id = ?" , id);
	}

	@Override
	public int del(Class<?>clz, String[] ids) {
		if(ids.length == 0)return 0;
		StringBuilder tmp = new StringBuilder();
		for(int i = 0, len = ids.length; i < len; i++){
			if(i > 0)tmp.append(",");
			tmp.append("?");
		}		
		return update("delete from " + clz.getName() + " where id in (" + tmp + ")", (Object[])ids);
	}

	@Override
	public <K>void update(K entity) {
		template.update(entity);
	}
	
	@Override
	public <K>K queryById(Class<K>clz, String id) {
		return template.get(clz, id);
	}	

	@Override
	public int update(String hql, Object...params) {
		return template.bulkUpdate(hql ,params);		
	}
	
	@Override
	public <K>List<K> query(String hql, Object...params) {
		return template.find(hql, params);
	}
	
	@Override
	public <K>List<K> query(final String hql, final int start, final int limit, final Object...params){
		return template.executeFind(new HibernateCallback<List<K>>() {
			@Override
			public List<K> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				if(params.length > 0)q.setParameters(params, getTypes(params));
				q.setFirstResult(start);
				q.setMaxResults(limit);
				return q.list();
			}
		});
	}
	
//-----------------------------------------------------------------------------------------------------------
	@Override
	public int sqlUpdate(final String sql, final Object...params) {
		return template.execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createSQLQuery(sql).setParameters(params, getTypes(params)).executeUpdate();
			}
		});
	}
	
	@Override
	public int[]sqlBatchUpdate(final String sql, final List<Object[]>params){
		return template.execute(new HibernateCallback<int[]>() {
			@Override
			public int[] doInHibernate(Session session) throws HibernateException,
					SQLException {
				PreparedStatement psmt = null;
				try {
					@SuppressWarnings("deprecation")
					Connection conn = session.connection();
					psmt = conn.prepareStatement(sql);
					for(Object[]p : params){//TODO 未做拆分
						for(int i = 0, len = p.length; i < len; i++){
							psmt.setObject(i + 1,  p[i]);
						}
						psmt.addBatch();
					}
					return psmt.executeBatch();
				} finally {
					if(psmt != null)psmt.close();
				}
			}
		});
	}
	
	@Override
	public <K>List<K>sqlList(String sql, Object...params){
		return sqlList(-1, 0, sql, params);
	}
	
	@Override
	public <K>List<K>sqlList(int start, int limit, String sql, Object...params){
		return queryBySql0(sql, null, start, limit, null, false, params);
	}	
	
	@Override
	public <K> List<K>sqlList(Class<K> clz, String sql, Object...params){
		return sqlList(-1, 0, clz, sql, params);
	}
	
	@Override
	public <K> List<K>sqlList(String[]fieldNames, Class<K> clz, String sql, Object...params){
		return queryBySql0(sql, fieldNames, -1, -1, clz, false, params);
	}
	
	@Override
	public <K> List<K>sqlList(int start, int limit, Class<K> clz, String sql, Object...params){
		return queryBySql0(sql, null, start, limit, clz, false, params);
	}
	
	@Override
	public <K> List<K> sqlList(int start, int limit, String[] fieldNames, Class<K> clz, String sql, Object... params) {
		return queryBySql0(sql, fieldNames, start, limit, clz, false, params);
	}
	
	@Override
	public List<Map<String, Object>>sqlListOfMap(String[]columns, String sql, Object...params){
		return sqlListOfMap(-1, 0, columns, sql, params);
	}

	@Override
	public List<Map<String, Object>>sqlListOfMap(int start, int limit, String[]columns, String sql, Object...params){
		return queryBySql0(sql, columns, start, limit, null, true, params);
	}
//--------------------------------------------------------------------------------------
	
	//start < 0 || limit <= 0查询所有
	private <K>List<K> queryBySql0(final String sql, final String[]columns, final int start, final int limit, final Class<K> clz, final boolean isMap, final Object...params){
		return template.execute(new HibernateCallback<List<K>>() {
			@Override
			public List<K> doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery q = session.createSQLQuery(sql);

				if(clz != null){
					if(columns == null)q.addEntity(clz);
					else
						q.setResultTransformer(new AliasToBeanTransformer(clz, columns));
				} else if(isMap){
					if(columns == null){
						q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					}else{
						q.setResultTransformer(new AliasToMapTransformer(columns));
					}
				}
				if(start >= 0 && limit > 0){
					q.setFirstResult(start);
					q.setMaxResults(limit);
				}				
				
				List<K>data;
				if(params.length == 0){
					data = q.list();
				}else{
					data = q.setParameters(params, getTypes(params)).list();
				}				
				return data;
			}
		});
	}
	
	private static final Map<Class<?>, org.hibernate.type.Type>typeMapping = new HashMap<Class<?>, org.hibernate.type.Type>();
	static {
		typeMapping.put(Byte.class, Hibernate.BYTE);
		typeMapping.put(Character.class, Hibernate.CHARACTER);
		typeMapping.put(Short.class, Hibernate.SHORT);
		typeMapping.put(Integer.class, Hibernate.INTEGER);
		typeMapping.put(Long.class, Hibernate.LONG);		
		typeMapping.put(Float.class, Hibernate.FLOAT);
		typeMapping.put(Double.class, Hibernate.DOUBLE);
		typeMapping.put(java.math.BigDecimal.class, Hibernate.BIG_DECIMAL);
		typeMapping.put(Boolean.class, Hibernate.TRUE_FALSE);
		typeMapping.put(java.util.Date.class, Hibernate.TIMESTAMP);
		typeMapping.put(java.sql.Date.class, Hibernate.TIMESTAMP);
		typeMapping.put(java.sql.Time.class, Hibernate.TIME);
		typeMapping.put(java.sql.Timestamp.class, Hibernate.TIMESTAMP);
		typeMapping.put(java.util.Calendar.class, Hibernate.TIMESTAMP);
		typeMapping.put(String.class, Hibernate.STRING);
	}
	private org.hibernate.type.Type[]getTypes(Object[]params){
		org.hibernate.type.Type[]result = new org.hibernate.type.Type[params.length];
		for(int i = 0, len = params.length; i < len; i++){
			result[i] = typeMapping.get(params[i].getClass());
		}
		
		return result;
	}
	//获取insert的SQL，ID是最后一个参数
	private static Method getSQLInsertStrings;
	private String buildInsertSQL(EntityPersister persister/*, Class<?>clz, Session session*/) throws Exception{
		/*Field f = AbstractEntityTuplizer.class.getDeclaredField("getters");
		f.setAccessible(true);
		Getter[]getters = (Getter[]) f.get(persister.getEntityMetamodel().getTuplizer(session.getEntityMode()));
		StringBuilder sql = new StringBuilder("insert into ");
		Table table = clz.getAnnotation(Table.class);
		sql.append(table == null || table.name().length() == 0 ? clz.getName() : table.name()).append("(").append(persister.getIdentifierPropertyName());
		
		String[]propertyNames = persister.getPropertyNames();
		for(int i = 0, len = getters.length; i < len; i++){
			sql.append(",");
			Method m = getters[i].getMethod();

			Column column = m.getAnnotation(Column.class);
			sql.append(column == null || column.name().length() == 0 ? propertyNames[i] : column.name());
		}
		sql.append(") values(?");
		for(int i = 0, len = getters.length; i < len; i++)sql.append(",?");
		sql.append(")");
		
		System.out.println(sql);
		return sql.toString();*/
		if(getSQLInsertStrings == null){//无需考虑并发
			getSQLInsertStrings = AbstractEntityPersister.class.getDeclaredMethod("getSQLInsertStrings");
			getSQLInsertStrings.setAccessible(true);
		}
		return ((String[])getSQLInsertStrings.invoke(persister))[0];
	}
}
