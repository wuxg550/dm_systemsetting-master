package com.hy.zookeeper.config.model;

import java.io.Serializable;

import com.hy.zookeeper.config.entity.PlatformServerType;

/**
 * 流向导图节点数据模型
 * @author jianweng
 *
 */
public class NodeDataModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6327648812872828835L;

	private String key;
	private String text;
	private String category = "Step";
	
	private String id;
	private String name;
	private String parent;
	private String type = "";
	
	public NodeDataModel() {
		super();
	}
	
	public NodeDataModel(PlatformServerType t) {
		super();
		this.key = t.getServerType();
		this.text = t.getServerType();
		
		this.id = t.getServerType();
		this.name = t.getServerType();
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
