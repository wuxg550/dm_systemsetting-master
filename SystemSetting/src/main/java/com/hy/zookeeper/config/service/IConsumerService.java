package com.hy.zookeeper.config.service;

import java.util.List;

import com.hy.zookeeper.config.entity.Consumer;

/**
 * 消费功能业务层
 * @author jianweng
 *
 */
public interface IConsumerService {

	/**
	 * 获取服务消费功能
	 * @param serverType
	 * @return
	 */
	public List<Consumer> getConsumer(String serverType);
}
