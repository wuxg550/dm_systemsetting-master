package cn.hy.config.zkclient.test;

import java.util.List;

import cn.hy.config.zkclient.zknode.interfaces.IAddress;
import cn.hy.config.zkclient.zknode.interfaces.IEntrances;
import cn.hy.config.zkclient.zknode.interfaces.IServerInfo;

public class MyServerInfoTest implements IServerInfo, IEntrances, IAddress {

	private String serverId;
	private String serverType;
	//服务名
	private String serverName;
	//机构编码
	private String orgCode;
	private String addressType;
	private String address;
	private List<MyEntranceTest> entrances;
	
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
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@SuppressWarnings("unchecked")
	public List<MyEntranceTest> getEntrances() {
		return entrances;
	}
	public void setEntrances(List<MyEntranceTest> entrances) {
		this.entrances = entrances;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	@Override
	public String getCascadeDomain() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getOrgId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getOrgName() {
		// TODO Auto-generated method stub
		return null;
	}
}
