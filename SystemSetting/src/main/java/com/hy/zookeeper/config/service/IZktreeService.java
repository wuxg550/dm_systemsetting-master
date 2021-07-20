package com.hy.zookeeper.config.service;

import java.util.List;

import com.hy.zookeeper.config.common.TreeNode;
import com.hy.zookeeper.config.common.ZkNode;
import com.hy.zookeeper.config.entity.Zkserver;

public interface IZktreeService {
	
	/**
	 * 获取所有的zookeeperclient
	 * @return
	 */
	List<Zkserver> finAllServer();
	
	/**
	 * 获取server 节点信息
	 * @param path 获取该路径下的节点
	 * @param ip zookeeper ip 
	 * @param port zookeeper 端口
	 * @return
	 */
	List<TreeNode> getZkTree(String path, String ip, String port);
	
	
    /**
     * 获取某个path下面的节点以及节点的数据
     * @param path 节点路径
     * @return
     */
	List<ZkNode> getZKTreeData(String path);

}
