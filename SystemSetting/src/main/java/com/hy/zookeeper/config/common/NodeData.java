package com.hy.zookeeper.config.common;

import com.hy.zookeeper.config.enums.NodeModelEnum;


/**      
 * 实现功能： 节点数据， 
 * @version    
 */
public class NodeData {
	/**
	 * 节点数据
	 */
	private String data;
	/**
	 * 节点类型
	 */
	private NodeModelEnum nodeModel;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public NodeModelEnum getNodeModel() {
		return nodeModel;
	}

	public void setNodeModel(NodeModelEnum nodeModel) {
		this.nodeModel = nodeModel;
	}

}
