package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 流向列表
 * @author hrh
 *
 */
@Entity
@Table(name="BASISDATA1.platform_server_relation")
public class ServerRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//	流向id
	private String relationName;//	流向名
	private String srcServerType;//	所属服务类型
	private String srcServerId;//	所属服务ID
	private String srcConsumerFc;//	消费功能码
	private String destServerType;//	目标服务类型
	private String destServerId;//	目标服务id
	private String destProviderFc;//	目标服务提供功能码
	private String destEntranceId;//	目标服务端口id
	private String destIp;//	目标服务IP地址
	private Integer destPort;//	目标服务通道端口
	private String destProtocol;//	目标服务通道协议类型
	private String destUrl;//	目标服务通道地址
//	private String destPageUrl;//	界面地址
	private String userName;
	private String password;
	
	// 以下非数据库字段
	private String srcIp;
	private String srcServerName; // 源服务名
	private String destServerName; // 目标服务名
	/**
	 *  目标服务在离线标识  0在线，1离线
	 */
	private Integer destOnlineStatus;
	
	
	public ServerRelation(ServerEntrance e) {
		super();
		this.destServerType = e.getServerType();
		this.destServerId = e.getServerId();
		this.destEntranceId = e.getId();
		this.destPort = e.getPort();
		this.destProtocol = e.getProtocol();
		this.destUrl = e.getURL();
		this.userName = e.getUserName();
		this.password = e.getPassword();
	}
	public ServerRelation() {
		super();
	}
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name ="relation_name")
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	@Column(name ="src_server_type")
	public String getSrcServerType() {
		return srcServerType;
	}
	public void setSrcServerType(String srcServerType) {
		this.srcServerType = srcServerType;
	}
	@Column(name ="src_server_id")
	public String getSrcServerId() {
		return srcServerId;
	}
	public void setSrcServerId(String srcServerId) {
		this.srcServerId = srcServerId;
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
	@Column(name ="dest_server_id")
	public String getDestServerId() {
		return destServerId;
	}
	public void setDestServerId(String destServerId) {
		this.destServerId = destServerId;
	}
	@Column(name ="dest_provider_fc")
	public String getDestProviderFc() {
		return destProviderFc;
	}
	public void setDestProviderFc(String destProviderFc) {
		this.destProviderFc = destProviderFc;
	}
	@Column(name ="dest_entrance_id")
	public String getDestEntranceId() {
		return destEntranceId;
	}
	public void setDestEntranceId(String destEntranceId) {
		this.destEntranceId = destEntranceId;
	}
	@Column(name ="dest_ip")
	public String getDestIp() {
		return destIp;
	}
	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}
	@Column(name ="dest_port")
	public Integer getDestPort() {
		return destPort;
	}
	public void setDestPort(Integer destPort) {
		this.destPort = destPort;
	}
	@Column(name ="dest_protocol")
	public String getDestProtocol() {
		return destProtocol;
	}
	public void setDestProtocol(String destProtocol) {
		this.destProtocol = destProtocol;
	}
	@Column(name ="dest_url")
	public String getDestUrl() {
		return destUrl;
	}
	public void setDestUrl(String destUrl) {
		this.destUrl = destUrl;
	}
	
	@Column(name ="user_name") 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name ="password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name ="dest_online_status")
	public Integer getDestOnlineStatus() {
		return destOnlineStatus;
	}
	public void setDestOnlineStatus(Integer destOnlineStatus) {
		this.destOnlineStatus = destOnlineStatus;
	}
	
	@Transient
	public String getSrcServerName() {
		return srcServerName;
	}
	public void setSrcServerName(String srcServerName) {
		this.srcServerName = srcServerName;
	}
	
	@Transient
	public String getDestServerName() {
		return destServerName;
	}
	public void setDestServerName(String destServerName) {
		this.destServerName = destServerName;
	}
	
	@Transient
	public String getSrcIp() {
		return srcIp;
	}
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	
}
