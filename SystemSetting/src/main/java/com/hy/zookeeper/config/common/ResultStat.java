package com.hy.zookeeper.config.common;

/**
 * 关于性能和连接的客户端的信息。
 * @author Administrator
 *
 */
public class ResultStat {
	
	private String zookeeperVersion;
	
	private String mode;
	
	private int connections;
	
	private int nodeCount;

	public String getZookeeperVersion() {
		return zookeeperVersion;
	}

	public void setZookeeperVersion(String zookeeperVersion) {
		this.zookeeperVersion = zookeeperVersion;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}
	

}
