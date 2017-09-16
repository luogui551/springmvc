package com.aires.base.page;

import javax.servlet.http.HttpServletRequest;

/**
 * 分页可能用到的工具类
 * @author User
 *
 */
public class PageUtils {
	/**
	 * 从request中获取pageNo和pageSize
	 * @param request
	 * @return {pageNo, pageSize},默认{1,10}
	 */
	public static int[]getPageNoAndPageSize(HttpServletRequest request){
		return PageUtils.getPageNoAndPageSize(request, 1, 10);
	}
	/**
	 * 从request中获取pageNo和pageSize
	 * @param request
	 * @param defaultPageNo 默认值
	 * @param defaultPageSize 默认值
	 * @return {pageNo, pageSize}
	 */
	public static int[]getPageNoAndPageSize(HttpServletRequest request, int defaultPageNo, int defaultPageSize){
		try{
			defaultPageNo = Integer.parseInt(request.getParameter("pageNo"));
		}catch(Exception e){}
		try{
			defaultPageSize = Integer.parseInt(request.getParameter("pageSize"));
		}catch(Exception e){}
		return new int[]{defaultPageNo, defaultPageSize};
	}
}
