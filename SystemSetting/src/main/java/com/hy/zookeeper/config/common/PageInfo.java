package com.hy.zookeeper.config.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分页信息类  GRID分页参数
 * @author 
 *
 */
public class PageInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//返回参数 总记录数
	private long total=-1;
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}

	//兼容easyui返回参数 当前页
	private int page;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	//返回参数 每页记录数
	private int rows;
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	@JsonIgnore
	public int getPageIndex() {
		return getPage();
	}
	@JsonIgnore
	public void setPageIndex(int pageIndex) {
		this.page = pageIndex;
	}
	@JsonIgnore
	public int getPageSize() {
		return getRows();
	}
	@JsonIgnore
	public void setPageSize(int pageSize) {
		this.rows = pageSize;
	}
	@JsonIgnore
	public long getAllSize() {
		return getTotal();
	}
	@JsonIgnore
	public void setAllSize(long allSize) {
		this.total = allSize;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	@JsonIgnore
	public int getFirstResult(){
		int firstResult = (getPageIndex() - 1) * getPageSize();
		if (firstResult >= getAllSize()) {
			firstResult = 0;
		}
		return firstResult;
	}
	
	/**
	 * 获取 Hibernate MaxResults
	 */
	@JsonIgnore
	public int getMaxResults(){
		return getPageSize();
	}
	/**
	 * 是否还没有统计过,没统计过的 allSize为 -1，
	 * 当不为-1时表示统计过，查询分页数据时不再统计 所以当查询条件有变化时，设置allSize为-1
	 * @return
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.total==-1;
	}
}
