package com.aires.base.page;

import java.util.Collections;
import java.util.List;
/**
 * 分页类
 * @author User
 *
 * @param <T>
 */
public class PagedList<T> {
	private int pageNo = 1;
	private int pageSize = 10;
	private List<T> result = Collections.emptyList();
	private int totalCount = 0;
	private int totalPage = 0;	
	
	public PagedList(int pageNo, int pageSize) {
		super();
		if(pageNo > 0)
		this.pageNo = pageNo;
		if(pageSize > 0)
		this.pageSize = pageSize;
	}
	public PagedList(int pageNo, int pageSize, List<T> result, int totalCount,
			int totalPage) {
		super();
		if(pageNo > 0)
			this.pageNo = pageNo;
			if(pageSize > 0)
			this.pageSize = pageSize;
		this.result = result;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
	}
	public PagedList() {
		super();
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		if(pageNo > 0)
			this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		if(pageSize > 0)
			this.pageSize = pageSize;
	}
	public List<T> getRows() {
		return result;
	}
	public void setRows(List<T> result) {
		this.result = result;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPage() {
		if(totalPage == 0){
			totalPage = (this.totalCount + this.pageSize - 1) / this.pageSize;
		}
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}	
	
	public int getStart(){
		return (this.pageNo - 1) * this.pageSize;
	}
	public int getEnd(){
		return this.pageNo * this.pageSize;
	}
	public int getNextPage(){
		return getHasNext() ? (pageNo + 1) : pageNo;
	}
	public int getPrePage(){
		return getHasPre() ? (pageNo - 1) : pageNo;
	}
	public boolean getHasPre(){
		return pageNo != 1;
	}
	public boolean getHasNext(){
		return pageNo != this.getTotalPage();
	}
}
