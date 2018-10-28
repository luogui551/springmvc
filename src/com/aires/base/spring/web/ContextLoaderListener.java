package com.aires.base.spring.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.aires.base.spring.ds.DynamicDataSource;
import com.aires.base.spring.util.SpringUtils;
import com.aires.hibernate.util.HibernateUtils;
/**
 * 支持一些定制化的操作
 * @author aires
 * 2016-11-2 下午4:16:08
 * 描述：
 */
public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener{

	@Override
	public void contextInitialized(ServletContextEvent event) {
		HibernateUtils.fixOracleChar();
		
		ServletContext ctx = event.getServletContext();
		ctx = (ServletContext) Proxy.newProxyInstance(ctx.getClass().getClassLoader(), new Class<?>[]{ServletContext.class}, new Handler(ctx));
		
		super.contextInitialized(new ServletContextEvent(ctx));
		
		SpringUtils.setWebApplicationContext(WebApplicationContextUtils.getWebApplicationContext(ctx));
		SpringUtils.getBean(DynamicDataSource.class).init();
	}
	
	private class Handler implements InvocationHandler {
		
		private ServletContext ctx;
		
		public Handler(ServletContext ctx){
			this.ctx = ctx;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if(args != null && args.length == 1 && "contextConfigLocation".equals(args[0])){
				String params = ctx.getInitParameter("contextConfigLocation"), innerXMLFile = "classpath*:cfg/spring.xml";
				return params == null ? innerXMLFile : (params + "," + innerXMLFile);
			}
			return method.invoke(ctx, args);
		}
		
	}
	
		
}
