package cn.hy.config.zkclient.zknode.interfaces;

/**
 * 服务信息节点数据接口
 * @author jianweng
 *
 */
public interface IServerInfo extends IServerKeyPoint{
	/**
	 * 服务名
	 * @return
	 */
	public String getServerName();
	
	/**
	 * 服务所属机构功能码
	 * @return
	 */
	public String getOrgCode();
	
	/**
	 * 获取服务的级联域
	 * @return
	 */
	public String getCascadeDomain();
	
	/**
	 * 获取服务的机构id
	 * @return
	 */
	public String getOrgId();
	
	/**
	 * 获取服务的机构名
	 * @return
	 */
	public String getOrgName();
}
