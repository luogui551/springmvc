package com.aires.base.spring.web;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aires.base.util.ResponseData;
import com.aires.base.util.ResponseUtil;
/**
 * 1、设置默认配置文件路径
 * 2、异常处理
 * @author User
 *
 */
public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	@Override
	protected void doDispatch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try{
			super.doDispatch(request, response);
		}catch(Throwable e){
			e.printStackTrace();
			ResponseUtil.write(response, new ResponseData(false, e.getMessage()));
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(new ServletConfigProxy(config));
	}
	
	private class ServletConfigProxy implements ServletConfig {
		
		private ServletConfig config;
		
		public ServletConfigProxy(ServletConfig config){
			this.config = config;
		}
		@Override
		public String getInitParameter(String name) {
			return "contextConfigLocation".equals(name) ? "classpath:cfg/springmvc.xml" : config.getInitParameter(name);
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return new ParameterNames(config.getInitParameterNames(), "contextConfigLocation");
		}

		@Override
		public ServletContext getServletContext() {
			return config.getServletContext();
		}

		@Override
		public String getServletName() {
			return config.getServletName();
		}
		
	}
	//简单实现的一个在指定Enumeration<String>后拼接一个String的Enumeration<String>
	private class ParameterNames implements Enumeration<String> {
		
		private Enumeration<String>target;
		
		private String toContact;
		//表示toContact是否已读取
		private boolean lastRead = false;
		
		public ParameterNames(Enumeration<String>target, String toContact){
			this.target = target;
			this.toContact = toContact;
		}

		@Override
		public boolean hasMoreElements() {
			return target.hasMoreElements() || !lastRead;
		}

		@Override
		public String nextElement() {
			if(target.hasMoreElements()){
				return target.nextElement();
			}
			lastRead = true;
			return toContact;
		}
		
	}

}
