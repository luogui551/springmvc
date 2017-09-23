package com.aires.hibernate.util;

import java.sql.Types;

import org.hibernate.Hibernate;

/**
 * @author aires
 * @date 2016年10月21日 下午4:05:18
 * 功能描述：Types.CHAR映射为Hibernate.STRING(解决在某种特定情况下，可能只会读取数据库数据的一个字符问题)
 */
public class Oracle10gDialect extends org.hibernate.dialect.Oracle10gDialect{

	@Override
	protected void registerHibernateType(int code, String name) {
		if(code == Types.CHAR){
			name = Hibernate.STRING.getName();
		}
		super.registerHibernateType(code, name);
	}

}
