package cn.hy.config.zkclient.zknode;

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
}
