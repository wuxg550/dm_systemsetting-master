package com.hy.zookeeper.config.entity;

import javax.persistence.Column;
import javax.persistence.Id;
/**
 * 服务列表
 * 
 */
//@Entity
public class Zkserver implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String ip;
	
	private String port;
	
	private String path;
	
	private String status;

	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
