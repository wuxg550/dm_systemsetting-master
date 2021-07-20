package com.hy.zookeeper.config.model;

import com.hy.zookeeper.config.entity.ServerRelation;

public class RelationTreeGrid {

	
	private String id; // 
	private String parent; // serverId
	private Integer level;
    private Boolean isLeaf = false;
    private Boolean loaded = true;
    private Boolean expanded = true;
	
	private String relationName;//	流向名
	private String srcServerType;//	所属服务类型
	private String srcServerId;//	所属服务ID
	private String srcConsumerFc;//	消费功能码
	private String destServerType;//	目标服务类型
	private String destServerId;//	目标服务id
	private String destProviderFc;//	目标服务提供功能码
	
	public RelationTreeGrid() {
		super();
	}
	
	public RelationTreeGrid(ServerRelation r) {
		this.id = r.getId();
		this.parent = r.getSrcServerId();
		this.level = 2;
		this.isLeaf = true;
		this.relationName = r.getRelationName();
		this.srcServerType = r.getSrcServerType();
		this.srcServerId = r.getSrcServerId();
		this.srcConsumerFc = r.getSrcConsumerFc();
		this.destServerType = r.getDestServerType();
		this.destServerId = r.getDestServerId();
		this.destProviderFc = r.getDestProviderFc();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Boolean getLoaded() {
		return loaded;
	}
	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public String getSrcServerType() {
		return srcServerType;
	}
	public void setSrcServerType(String srcServerType) {
		this.srcServerType = srcServerType;
	}
	public String getSrcServerId() {
		return srcServerId;
	}
	public void setSrcServerId(String srcServerId) {
		this.srcServerId = srcServerId;
	}
	public String getSrcConsumerFc() {
		return srcConsumerFc;
	}
	public void setSrcConsumerFc(String srcConsumerFc) {
		this.srcConsumerFc = srcConsumerFc;
	}
	public String getDestServerType() {
		return destServerType;
	}
	public void setDestServerType(String destServerType) {
		this.destServerType = destServerType;
	}
	public String getDestServerId() {
		return destServerId;
	}
	public void setDestServerId(String destServerId) {
		this.destServerId = destServerId;
	}
	public String getDestProviderFc() {
		return destProviderFc;
	}
	public void setDestProviderFc(String destProviderFc) {
		this.destProviderFc = destProviderFc;
	}
}
