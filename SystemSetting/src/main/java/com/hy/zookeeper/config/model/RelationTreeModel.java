package com.hy.zookeeper.config.model;

import java.util.ArrayList;
import java.util.List;

import com.hy.zookeeper.config.entity.ServerRelation;

/**
 * 流向树形表格模型
 * @author jianweng
 *
 */
public class RelationTreeModel {

	private String id; // 
	private String pid; // serverId
	private String relationName;//	流向名
	private String srcServerType;//	所属服务类型
	private String srcServerId;//	所属服务ID
	private String srcConsumerFc;//	消费功能码
	private String destServerType;//	目标服务类型
	private String destServerId;//	目标服务id
	private String destProviderFc;//	目标服务提供功能码
	List<RelationTreeModel> children = new ArrayList<>();
	
	private String iconCls;
	private String state;
	private boolean checked = false;
	
	private String srcServerName;
	private String destServerName;
	
	public RelationTreeModel() {
		super();
	}
	
	public RelationTreeModel(ServerRelation r) {
		this.id = r.getId();
		this.relationName = r.getRelationName();
		this.srcServerType = r.getSrcServerType();
		this.srcServerId = r.getSrcServerId();
		this.pid = r.getSrcServerId();
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
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
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
	public List<RelationTreeModel> getChildren() {
		return children;
	}
	public void setChildren(List<RelationTreeModel> children) {
		this.children = children;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public String getSrcServerName() {
		return srcServerName;
	}

	public void setSrcServerName(String srcServerName) {
		this.srcServerName = srcServerName;
	}

	public String getDestServerName() {
		return destServerName;
	}

	public void setDestServerName(String destServerName) {
		this.destServerName = destServerName;
	}
}
