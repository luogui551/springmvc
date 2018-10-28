package com.aires.base.service.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.aires.base.dao.DAO;
import com.aires.base.exception.UnexpectedException;

/**
 * @author aires
 * 2017年9月22日 下午3:55:01
 * 描述：
 */
public abstract class BaseServiceImpl<X extends DAO> {
	/**
	 * 获取支持service的dao实例
	 * @return
	 */
	protected abstract X getDao();
	
	/**
	 * 检测list是否只有一个对象，并返回该对象，否则抛出异常
	 * @param list
	 * @return
	 */
	protected <K>K requiredSingleResult(List<K>list){
		if(list.size() == 1){
			return list.get(0);
		}
		throw new UnexpectedException("期望的记录数为1，实际" + (list.size() == 0 ? "为0" : "大于1") + "！");
	}
	/**
	 * 空指针检测
	 * @param o
	 */
	protected void checkNull(Object o){
		if(o == null)throw new NullPointerException();
	}
	/**
	 * 分页参数检测
	 * @param pageNum
	 * @param pageSize
	 */
	protected void checkPageArgs(int pageNum, int pageSize){
		if(pageNum <=0 || pageSize <= 0)throw new IllegalArgumentException("分页参数不正确：pageNum:" + pageNum + ",pageSize:" + pageSize);
	}
	/**
	 * 获取指定类、指定属性对应的get方法
	 * @param clz
	 * @param fieldName
	 * @return
	 */
	protected Method getGetter(Class<?>clz, String fieldName){
		fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method m = null;
		try {
			m = clz.getMethod("get" + fieldName);
		} catch (NoSuchMethodException e) {
			try {
				m = clz.getMethod("is" + fieldName);
			} catch (NoSuchMethodException e0) {
			}
		}
		return m;
	}
	/**
	 * 为?ql(如sql,hql等)语句添加order by
	 * @param s_hql
	 * @param orderBy
	 * @return
	 */
	protected String addOrderBy(String _ql, String orderBy){
		if(StringUtils.isNotEmpty(orderBy)){
			String key = " order by ";
			_ql += (orderBy.indexOf(key) == -1 ? key : "") + orderBy;
		}
		return _ql;
	}
}
