package com.hy.zookeeper.config.dto;

public class CommonServerInfo {

	// 级联域
	private String cascadeDomain;
	// 机构id
	private String orgId;
	// 机构名称
	private String orgName;
	
	public String getCascadeDomain() {
		return cascadeDomain;
	}
	public void setCascadeDomain(String cascadeDomain) {
		this.cascadeDomain = cascadeDomain;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
