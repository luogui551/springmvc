package com.aires.base.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletResponse;
/**
* Authored by aires at 2014年9月2日 上午11:39:23
* 功能：写回客户端数据用
* 版本：v1.0
 */
public class ResponseUtil {
	/**
	 * 默认编码格式
	 */
	public static final String CHARSET = "UTF-8";
	
	public static void write(ServletResponse response, Object content, String...contentTypes){
		try {
			ResponseUtil.write(response, JSONUtil.toJSON(content).getBytes(CHARSET), contentTypes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public static void write(ServletResponse response, byte[]content, String...contentTypes){
		String contentType;
		if(contentTypes.length > 0)contentType = contentTypes[0];
		else contentType = "application/json";
		response.setContentType(contentType);
		response.setContentLength(content.length);
		response.setCharacterEncoding(CHARSET);
		
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if(os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
