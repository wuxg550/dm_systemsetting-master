package com.hy.zookeeper.config.service;

import java.util.List;

import com.hy.zookeeper.config.entity.Zkserver;

public interface IZkserverService {

	List<Zkserver> findByPage(Zkserver zkserver, int page, int rows);

	boolean saveServer(Zkserver zkserver);

	Zkserver findZkserverById(String id);

	boolean delServer(String id);

	boolean initNode(String ip, String port);

	/**
	 * 获取zookeeper集群连接字符串  ip:port,...
	 * @return
	 */
	public String getConnectString();
}
