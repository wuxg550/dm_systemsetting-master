package com.hy.zookeeper.config.common;

import com.hy.zookeeper.config.enums.NodeModelEnum;

/**      
 * ZkNode   
 * @version    
 */
public class ZkNode implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 当前节点id 如 serviceA
	 */
	private String id;
	/**
	 * 父节点id 如 /service
	 */
	private String pId;
	/**
	 * 当前节点数据
	 */
	private String data;
	/**
	 * 当前节点的全部path，如 /service/serviceA
	 */
	private String fullId;
	/**
	 * 节点的类型
	 */
	private NodeModelEnum nodeModel;

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullId() {
		return fullId;
	}

	public void setFullId(String fullId) {
		this.fullId = fullId;
	}

	public NodeModelEnum getNodeModel() {
		return nodeModel;
	}

	public void setNodeModel(NodeModelEnum nodeModel) {
		this.nodeModel = nodeModel;
	}

}
