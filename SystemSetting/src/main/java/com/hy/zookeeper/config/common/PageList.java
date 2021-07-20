package com.hy.zookeeper.config.common;

import java.util.List;

public class PageList<T> {
	private int pageIndex;
	private int pageSize;
	private int allSize;
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getAllSize() {
		return allSize;
	}
	public void setAllSize(int allSize) {
		this.allSize = allSize;
	}
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	private List<T> resultList = null;//结果集
}
