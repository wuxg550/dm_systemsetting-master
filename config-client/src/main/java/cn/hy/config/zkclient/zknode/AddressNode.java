package cn.hy.config.zkclient.zknode;

import java.io.Serializable;

import cn.hy.config.zkclient.zknode.interfaces.IAddress;

/**
 * 地址节点实体类
 * @author jianweng
 *
 */
public class AddressNode implements Serializable{
	
	private static final long serialVersionUID = 7883156942859413940L;
	
	/**
	 * 地址类型（IP/ComputerName/DomainName）
	 */
	private String addressType;
	/**
	 * 地址值
	 */
	private String address;
	
	public AddressNode() {}
	public AddressNode(IAddress address) {
		this.addressType = address.getAddressType();
		this.address = address.getAddress();
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
}
