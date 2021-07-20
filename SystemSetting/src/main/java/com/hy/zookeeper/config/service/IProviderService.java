package com.hy.zookeeper.config.service;

import java.util.List;

import com.hy.zookeeper.config.entity.Provider;

/**
 * 提供功能业务层
 * @author jianweng
 *
 */
public interface IProviderService {

	/**
	 * 启用、禁用功能
	 * @param serverType
	 * @param fc
	 * @param state
	 * @return
	 */
	public boolean updateProviderState(String serverType, String fc, Integer state, String connectString);
	
	/**
	 * 得到提供功能列表
	 * @param serverType
	 * @return
	 */
	public List<Provider> getProvider(String serverType);
}
