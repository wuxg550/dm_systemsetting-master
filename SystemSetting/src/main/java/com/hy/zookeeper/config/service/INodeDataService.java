package com.hy.zookeeper.config.service;

import java.util.List;

import com.hy.zookeeper.config.common.ZkNode;
import com.hy.zookeeper.config.entity.Zkserver;

public interface INodeDataService {

	List<Zkserver> findAllZkServer();

	List<ZkNode> getNodeDataList(String node, String host);
	
	/**
	 * 从zookeeper服务同步所有节点数据
	 */
	public void syncNodeData();
	
	void deleteNode(String path);
}
