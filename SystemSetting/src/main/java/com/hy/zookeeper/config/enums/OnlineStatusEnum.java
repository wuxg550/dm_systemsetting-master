package com.hy.zookeeper.config.enums;

public enum OnlineStatusEnum {

	ONLINE(0, "在线"),
	OFFLINE(1, "离线");
	
	public Integer getStatus() {
		return status;
	}
	public String getRemark() {
		return remark;
	}
	private Integer status;
	private String remark;
	private OnlineStatusEnum(Integer status, String remark) {
		this.status = status;
		this.remark = remark;
	}
}
