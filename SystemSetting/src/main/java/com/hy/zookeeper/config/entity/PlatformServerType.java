package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 服务类型表
 * @author hrh
 *
 */
@Entity
@Table(name="BASISDATA1.platform_server_type")
public class PlatformServerType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//id 自增
	
	private String serverType;//服务类型
	
	private String serverTypeName;//服务类型中文解释
	
	private String serverRemark;//类型备注

	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@JSONField(name="serviceType")
	@Column(name ="server_type")
	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	
	@JSONField(name="typeName")
	@Column(name ="server_type_name")
	public String getServerTypeName() {
		return serverTypeName;
	}

	public void setServerTypeName(String serverTypeName) {
		this.serverTypeName = serverTypeName;
	}
	
	@JSONField(serialize=false)
	@Column(name ="type_remark")
	public String getServerRemark() {
		return serverRemark;
	}

	public void setServerRemark(String serverRemark) {
		this.serverRemark = serverRemark;
	}
	
}
