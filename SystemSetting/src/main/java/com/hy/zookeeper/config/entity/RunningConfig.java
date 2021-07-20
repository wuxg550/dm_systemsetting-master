package com.hy.zookeeper.config.entity;

import java.io.Serializable;

/**
 * 运行配置
 * @author jianweng
 *
 */
public class RunningConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 882417406526905136L;

	private String cfgkey;
	private String cfgval;
	private String cfgname;
	/**
	 * 值类型，1：密码
	 */
	private String valType;
	
	public String getCfgkey() {
		return cfgkey;
	}
	public void setCfgkey(String cfgkey) {
		this.cfgkey = cfgkey;
	}
	public String getCfgval() {
		return cfgval;
	}
	public void setCfgval(String cfgval) {
		this.cfgval = cfgval;
	}
	public String getCfgname() {
		return cfgname;
	}
	public void setCfgname(String cfgname) {
		this.cfgname = cfgname;
	}
	
	public String getValType() {
		return valType;
	}
	public void setValType(String valType) {
		this.valType = valType;
	}
}
