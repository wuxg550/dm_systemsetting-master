package com.hy.zookeeper.config.dto;

import java.io.Serializable;

/**
 * 流向传输类
 * @author jianweng
 *
 */
public class RelationDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2908894335436280017L;
	
	//流向名
	private String relationName;
	//源服务类型
	private String srcServerType;
	//源服务id
	private String srcServerId;
	//源服务消费功能码
	private String srcConsumerFc;
	//目标服务类型
	private String destServerType;
	//目标服务id
	private String destServerId;
	//目标提供功能码
	private String destProviderFc;
	//目标服务入口id
	private String destEntranceId;
	//目标IP地址
	private String destIp;
	//目标端口
	private Integer destPort;
	//目标服务入口协议
	private String destProtocol;
	//目标服务URL
	private String destUrl;
	
	private String userName;
	private String password;
	
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
	public String getDestEntranceId() {
		return destEntranceId;
	}
	public void setDestEntranceId(String destEntranceId) {
		this.destEntranceId = destEntranceId;
	}
	public String getDestIp() {
		return destIp;
	}
	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}
	public Integer getDestPort() {
		return destPort;
	}
	public void setDestPort(Integer destPort) {
		this.destPort = destPort;
	}
	public String getDestProtocol() {
		return destProtocol;
	}
	public void setDestProtocol(String destProtocol) {
		this.destProtocol = destProtocol;
	}
	public String getDestUrl() {
		return destUrl;
	}
	public void setDestUrl(String destUrl) {
		this.destUrl = destUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
