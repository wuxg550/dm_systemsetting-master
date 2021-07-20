package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hy.zookeeper.config.enums.ConfigFlagEnum;

/**
 * 服务信息表
 * @author hrh
 *
 */
@Entity
@Table(name="BASISDATA1.platform_server_info")
public class ServerInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;//服务ID
	
	private String domain;//服务所属域
	
	private String serverType;//服务类型
	
	private String serverName;//服务名
	
	private String orgCode;//机构编码
	
	private String serverIp;//IP地址/或域名
	
	private String status;//状态 0启用 1禁用
	
	private String confingFalg;// 0配置中心添加服务但没注册  1添加并且注册 2服务主动注册
	
	private Integer onlineStatus; //服务在离线标识  1离线，0在线

	private String addressType;
	
	// 级联域
	private String cascadeDomain;
	// 机构id
	private String orgId;
	// 机构名称
	private String orgName;
	// 版本号
	private String version;
	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name ="domain_")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name ="server_type") 
	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	@Column(name ="server_name") 
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Column(name ="org_code") 
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name ="address_value") 
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	@Column(name ="status") 
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="confing_falg")
	public String getConfingFalg() {
		return confingFalg;
	}

	public void setConfingFalg(String confingFalg) {
		this.confingFalg = confingFalg;
	}

	@Column(name="online_status")
	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	
	@Column(name="address_type")
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	
	@Column(name="cascade_domain")
	public String getCascadeDomain() {
		return cascadeDomain;
	}
	public void setCascadeDomain(String cascadeDomain) {
		this.cascadeDomain = cascadeDomain;
	}
	
	@Column(name="org_id")
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Column(name="org_name")
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name="version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Transient
	public boolean isRemoteServer(){
		return ConfigFlagEnum.REMOTE.getFlag().equals(this.confingFalg);
	}
}
