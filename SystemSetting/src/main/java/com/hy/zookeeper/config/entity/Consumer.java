package com.hy.zookeeper.config.entity;

import javax.persistence.Column;
import javax.persistence.Id;


/**
 * 输出集合
 * @author hrh
 *
 */
//@Entity
//@Table(name="platform_server_consumer")
public class Consumer implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;//	主键
	private String serverType;//	服务类型
	private String protocol;//	消费协议类型
	private String fc;//	功能码
	private String funDescribe;//	功能描述
	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="server_type")
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	@Column(name="protocol")
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	@Column(name="fc")
	public String getFc() {
		return fc;
	}
	public void setFc(String fc) {
		this.fc = fc;
	}
	@Column(name="fun_describe")
	public String getFunDescribe() {
		return funDescribe;
	}
	public void setFunDescribe(String funDescribe) {
		this.funDescribe = funDescribe;
	}
		

}
