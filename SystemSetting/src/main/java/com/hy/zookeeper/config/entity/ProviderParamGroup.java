package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
/**
 * 服务提供功能表-子表（参数元数据分组信息）
 * @author hrh
 *
 */
//@Entity
//@Table(name="platform_server_provider_param_group")
public class ProviderParamGroup implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//	分组ID
	private String groupName;//	分组名
	private Integer extendGroupId;//	继承分组id
	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name ="group_name")
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Column(name ="extend_group_id")
	public Integer getExtendGroupId() {
		return extendGroupId;
	}
	public void setExtendGroupId(Integer extendGroupId) {
		this.extendGroupId = extendGroupId;
	}
	
	
	


}
