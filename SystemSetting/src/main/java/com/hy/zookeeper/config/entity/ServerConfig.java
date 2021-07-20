package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;


/**
 * 服务私有配置表
 * @author hrh
 *
 */
//@Entity
//@Table(name="platform_server_config")
public class ServerConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String id;//	私有配置id
	private String serverId;//	所属服务id
	private String configName;//	配置键描述
	private String configKey;//	配置键
	private String configVal;//	配置值
	
	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name ="server_id") 
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	@Column(name ="config_name") 
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	@Column(name ="config_key") 
	public String getConfigKey() {
		return configKey;
	}
	
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	@Column(name ="config_val") 
	public String getConfigVal() {
		return configVal;
	}
	public void setConfigVal(String configVal) {
		this.configVal = configVal;
	}
	
	
	
	


}
