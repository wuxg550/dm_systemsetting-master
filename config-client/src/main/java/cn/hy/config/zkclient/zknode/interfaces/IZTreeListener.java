package cn.hy.config.zkclient.zknode.interfaces;

/**
 * zookeeper树路径监听接口
 * @author jianweng
 *
 */
public interface IZTreeListener {
	/**
	 * 新增节点
	 * @param data
	 */
	void nodeAdded(String nodeName,String data);
	/**
	 * 修改节点
	 * @param data
	 */
	void nodeUpdated(String nodeName,String data);
	/**
	 * 删除节点
	 * @param data
	 */
	void nodeRemoved(String nodeName);
}
