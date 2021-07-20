package com.hy.zookeeper.config.common;

import java.io.Serializable;

/**
 * jstree 数据工具类
 * @author hrh
 *
 */
public class TreeNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; 
	private String pId;
    private String text;
    private String state; 
    private boolean children;
    private String code;
    private String icon;
    private String remark;
   
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
 
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	 
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
  
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
 
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
 
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public boolean isChildren() {
		return children;
	}
	public void setChildren(boolean children) {
		this.children = children;
	}
	
	
}
