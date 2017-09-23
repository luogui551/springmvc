package com.aires.hibernate.dao;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.transform.ResultTransformer;
/**
 * 
 * @author aires
 * 2017年1月17日 下午3:06:38
 * 描述：用于支持别名
 */
@SuppressWarnings("serial")
abstract class AbstractTransformer implements ResultTransformer {
	
	private String[]aliases;
	
	protected AbstractTransformer(String[] aliases){
		this.aliases = aliases;
	}
	
	protected String[] getAliases(String[] aliases){
		return this.aliases == null ? aliases : this.aliases;
	}
	
	protected Object dealClob(Object v){
		if(v instanceof Clob){
			Clob val = (Clob)v;
			
			try {
				return val.getSubString(1l, (int)val.length());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return v;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List transformList(List collection) {
		return collection;
	}
}
