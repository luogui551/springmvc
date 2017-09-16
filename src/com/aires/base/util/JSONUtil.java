package com.aires.base.util;

import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author aires
 * 2017年2月13日 下午5:37:17
 * 描述：简单JSON工具类
 */
public class JSONUtil {
	/*
	 * 经测试，fastJSON解析json字符串性能更优，jackson对象转为json更优
	 * spring依赖于jackson，必须存在，fastJSON包较小，所以引入后分别调用两种实现处理
	 */
	/**
	 * json字符串转换为java中的List,Map
	 * @param jsonText
	 * @return 根据jsonText内容决定，返回Map,List
	 */
	@SuppressWarnings("unchecked")
	public static <K>K fromJSON(String jsonText) {
		return (K) JSON.parse(jsonText);
	}
	/**
	 * json字符串转换为对象
	 * @param json
	 * @param clz
	 * @return
	 */
	public static <K>K fromJSON(String jsonText, Class<K>clz) {
		return JSON.parseObject(jsonText, clz);
	}
	/**
	 * 对象转换为json字符串
	 * @param o
	 * @return
	 */	
	public static String toJSON(Object o){
		if(o == null)return "";
		if(o instanceof String)return (String) o;
		try {
			return mapper.writeValueAsString(o);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	private static final ObjectMapper mapper = new ObjectMapper();
}