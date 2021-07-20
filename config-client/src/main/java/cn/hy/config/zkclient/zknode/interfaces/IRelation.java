package cn.hy.config.zkclient.zknode.interfaces;

/**
 * 流向节点 数据结构接口
 * @author Administrator
 *
 */
public interface IRelation {
	/**
	 * 获取流向名
	 * @return
	 */
	public String getRelationName();
	/**
	 * 获取源服务类型
	 * @return
	 */
	public String getSrcServerType();
	/**
	 * 获取源服务id
	 * @return
	 */
	public String getSrcServerId();
	/**
	 * 获取原服务消费功能码
	 * @return
	 */
	public String getSrcConsumerFc();
	/**
	 * 获取目标服务类型
	 * @return
	 */
	public String getDestServerType();
	/**
	 * 获取目标服务id
	 * @return
	 */
	public String getDestServerId();
	/**
	 * 获取目标服务提供功能码
	 * @return
	 */
	public String getDestProviderFc();
	/**
	 * 获取目标入口id
	 * @return
	 */
	public String getDestEntranceId();
	/**
	 * 获取目标服务IP地址
	 * @return
	 */
	public String getDestIp();
	/**
	 * 获取目标服务端口
	 * @return
	 */
	public String getDestPort();
	/**
	 * 获取目标服务通讯协议
	 * @return
	 */
	public String getDestProtocol();
	/**
	 * 获取目标服务具体 URL/数据库名 等
	 * @return
	 */
	public String getDestUrl();
	
	/**
	 * 获取用户名
	 * @return
	 */
	public String getUserName();
	
	/**
	 * 获取密码
	 * @return
	 */
	public String getPassword();
}
