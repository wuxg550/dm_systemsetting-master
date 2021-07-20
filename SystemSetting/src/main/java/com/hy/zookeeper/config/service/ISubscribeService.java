package com.hy.zookeeper.config.service;

import org.apache.curator.framework.CuratorFramework;

/**
 * 订阅服务业务层
 * @author jianweng
 *
 */
public interface ISubscribeService {

	/**
	 * 订阅ip节点
	 * @param client
	 * @param path
	 */
	public void subscribeIpNode(CuratorFramework client, String path);
	
	/**
	 * 订阅入口节点
	 * @param client
	 * @param path
	 */
	public void subscribeEntranceNode(CuratorFramework client, String path);
	
	/**
	 * 订阅流向节点
	 * @param client
	 * @param path
	 */
	public void subscribeRelationNode(CuratorFramework client, String path);
}
