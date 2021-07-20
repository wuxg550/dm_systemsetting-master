package cn.hy.config.zkclient.zknode.interfaces;

public abstract interface IServerKeyPoint {
	/**
	 * 服务类型
	 * @return
	 */
	public String getServerType();
	
	/**
	 * 服务ID
	 * @return
	 */
	public String getServerId();
}
