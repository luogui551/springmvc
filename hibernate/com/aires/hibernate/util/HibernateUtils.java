/**
 * 
 */
package com.aires.hibernate.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.dialect.DialectFactory;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.dialect.DialectFactory.DatabaseDialectMapper;

/**
 * @author aires
 * 2016-10-31 下午7:33:27
 * 描述：
 */
public class HibernateUtils {
	/**
	 * 1、处理hibernate映射数据库char字段为单个字符问题
	 * 2、支持oracle11g
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static  void fixOracleChar(){
		try {
			Field f = DialectFactory.class.getDeclaredField("MAPPERS");
			f.setAccessible(true);
			
			Map map = (Map) f.get(null);
			map.put(
			        "Oracle",
			        new DatabaseDialectMapper() {
				        public String getDialectClass(int majorVersion) {
							switch ( majorVersion ) {
								case 8: return Oracle8iDialect.class.getName();
								case 9: return Oracle9iDialect.class.getName();
								case 10: return com.aires.hibernate.util.Oracle10gDialect.class.getName();
								case 11: return com.aires.hibernate.util.Oracle10gDialect.class.getName();
								default: throw new HibernateException( "unknown Oracle major version [" + majorVersion + "]" );
							}
				        }
			        }
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
