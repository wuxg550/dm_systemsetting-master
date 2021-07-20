package cn.hy.config.zkclient.zknode.interfaces;


/**
 * runningConfig节点数据变更监听
 * @author jianweng
 *
 */
public interface IRunningConfigListener {

	/**
	 * 
	 * @param data 节点数据
	 * @param path 节点路径
	 */
	void handleDataChange(String data, String path);
}
