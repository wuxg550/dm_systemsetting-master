package com.hy.zookeeper.config.dto;

/**
 * 服务信息传输类
 * @author jianweng
 *
 */
public class ServerInfoDto extends CommonServerInfo{

	//服务名
	private String serverName;
	//机构编码
	private String orgCode;
	//版本号
	private String version;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
}
