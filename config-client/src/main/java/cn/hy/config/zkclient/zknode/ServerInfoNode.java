package cn.hy.config.zkclient.zknode;

import cn.hy.config.zkclient.zknode.interfaces.IServerInfo;

/**
 * 服务信息传输类
 * @author jianweng
 *
 */
public class ServerInfoNode {

	//服务名
	private String serverName;
	//机构编码
	private String orgCode;
	
	
	public ServerInfoNode() {}
	public ServerInfoNode(IServerInfo info) {
		this.serverName = info.getServerName();
		this.orgCode = info.getOrgCode();
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
