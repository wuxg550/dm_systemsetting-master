package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BASISDATA1.platform_relation_templet")
public class RelationTemplet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//id UUID
	private String srcServerType;//	所属服务类型
	private String srcConsumerFc;//	消费功能码
	private String destServerType;//	目标服务类型
	private String destProviderFc;//	目标服务提供功能码
	private String description; // 描述
	
	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name ="src_server_type") 
	public String getSrcServerType() {
		return srcServerType;
	}
	public void setSrcServerType(String srcServerType) {
		this.srcServerType = srcServerType;
	}
	@Column(name ="src_consumer_fc") 
	public String getSrcConsumerFc() {
		return srcConsumerFc;
	}
	public void setSrcConsumerFc(String srcConsumerFc) {
		this.srcConsumerFc = srcConsumerFc;
	}
	@Column(name ="dest_server_type") 
	public String getDestServerType() {
		return destServerType;
	}
	public void setDestServerType(String destServerType) {
		this.destServerType = destServerType;
	}
	@Column(name ="dest_provider_fc") 
	public String getDestProviderFc() {
		return destProviderFc;
	}
	public void setDestProviderFc(String destProviderFc) {
		this.destProviderFc = destProviderFc;
	}

	@Column(name ="description") 
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
