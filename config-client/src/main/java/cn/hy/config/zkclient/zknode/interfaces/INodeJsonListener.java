package cn.hy.config.zkclient.zknode.interfaces;

/**
 * 流向节点监听接口
 * @author jianweng
 *
 */
public interface INodeJsonListener {

	/**
	 * 处理流向数据变化
	 * @param data 当前流向节点数据
	 * @throws Exception
	 */
	void handleDataChange(String data) throws Exception;
	
}
