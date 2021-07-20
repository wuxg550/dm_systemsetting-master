package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 服务端口功能映射表
 * @author hrh
 *
 */
//@Entity
//@Table(name="platform_entrance_function_map")
public class EntranceFunctionMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String serverId;
	
	private Integer port;
	
	private String providerFc;

	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name ="server_id") //有关联关系
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Column(name ="port") 
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Column(name ="provider_fc") 
	public String getProviderFc() {
		return providerFc;
	}

	public void setProviderFc(String providerFc) {
		this.providerFc = providerFc;
	}
	
	

}
