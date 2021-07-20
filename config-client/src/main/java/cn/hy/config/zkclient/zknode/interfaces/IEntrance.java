package cn.hy.config.zkclient.zknode.interfaces;

import java.util.List;

/**
 * 入口数据节点接口
 * @author jianweng
 *
 */
public interface IEntrance{
	/**
	 * 该入口端口
	 * @return
	 */
	public Integer getPort();
	
	/**
	 * 入口协议
	 * @return
	 */
	public String getProtocol();
	
	/**
	 * 入口url
	 * @return
	 */
	public String getUrl();
	
	/**
	 * 功能码集合
	 * @return
	 */
	public List<String> getFcs();
	
	/**
	 * 入口用户名
	 * @return
	 */
	public String getUserName();
	
	/**
	 * 入口密码
	 * @return
	 */
	public String getPassword();
}
