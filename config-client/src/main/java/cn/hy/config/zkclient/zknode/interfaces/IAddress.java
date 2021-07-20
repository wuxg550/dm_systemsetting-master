package cn.hy.config.zkclient.zknode.interfaces;

/**
 * 地址节点数据
 * @author jianweng
 *
 */
public interface IAddress extends IServerKeyPoint{
	/**
	 * 地址类型（IP等）
	 * @return
	 */
	public String getAddressType();
	
	/**
	 * 地址值
	 * @return
	 */
	public String getAddress();
}
