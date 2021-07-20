package com.hy.zookeeper.config.dto;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.hy.zookeeper.config.entity.ServerInfo;

public class AddressDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 334952478359616486L;
	
	private String addressType;
	private String address;
	
	public AddressDto() {
		super();
	}
	
	public AddressDto(ServerInfo serverInfo) {
		super();
		this.addressType = serverInfo.getAddressType();
		this.address = serverInfo.getServerIp();
	}
	
	@JSONField(name="addressType")
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	@JSONField(name="address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
