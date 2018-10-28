package com.aires.hibernate.dao;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author aires
 * 2017年1月17日 下午3:07:23
 * 描述：使用指定别名作为map的key
 */
@SuppressWarnings("serial")
class AliasToMapTransformer extends AbstractTransformer {

	public AliasToMapTransformer(String[]aliases) {
		super(aliases);
	}
	
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map<String, Object> result = new HashMap<String, Object>(tuple.length);
		aliases = this.getAliases(aliases);
		for ( int i = 0, len = Math.min(tuple.length, aliases.length); i < len; i++ ) {
			String alias = aliases[i];
			if ( alias != null ) {
				result.put( alias, dealClob(tuple[i]));
			}
		}
		return result;
	}
}