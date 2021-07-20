package com.hy.zookeeper.config.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.dto.EntranceDto;
import com.hy.zookeeper.config.util.StringUtil;
import com.hy.zookeeper.config.util.UUIDTOOL;

/**
 * 服务端口&协议表
 * @author hrh
 *
 */
@Entity
@Table(name="BASISDATA1.platform_server_entrance")
public class ServerEntrance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//	通道id
	private String serverType;//	服务类型
	private Integer port;//	通道端口
	private String protocol;//	通道协议类型
	private String URL;//	目标接口地址
	private String serverId; // 所属服务id
	private String fcs; // 功能码集合json串
	private String userName;
	private String password;
	
	public ServerEntrance() {
		super();
	}
	
	
	public ServerEntrance(String serverType, String serverId, EntranceDto dto) {
		super();
		if(StringUtil.isNotBlank(dto.getId())){
			this.id = dto.getId();
		}else{
			this.id = UUIDTOOL.getuuid(32);
		}
		this.serverType = serverType;
		this.port = dto.getPort();
		this.protocol = dto.getProtocol();
		this.URL = dto.getUrl();
		this.serverId = serverId;
		if(dto.getFcs() != null){
			this.fcs = StringUtil.replaceBlank(JSON.toJSONString(dto.getFcs()));
		}
		this.userName = dto.getUserName();
		this.password = dto.getPassword();
		
	}


	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name ="server_type") 
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	@Column(name ="port") 
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	@Column(name ="protocol") 
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	@Column(name ="url", length = 1024) 
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	
	@Column(name ="server_id") 
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Column(name ="fcs", columnDefinition="TEXT") 
	public String getFcs() {
		return fcs;
	}
	public void setFcs(String fcs) {
		this.fcs = fcs;
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
	
	@Transient
	public List<String> getFcList(){
		if(StringUtil.isNotBlank(this.fcs)){
			return JSON.parseArray(this.fcs, String.class);
		}else{
			return new ArrayList<>();
		}
	}
}
