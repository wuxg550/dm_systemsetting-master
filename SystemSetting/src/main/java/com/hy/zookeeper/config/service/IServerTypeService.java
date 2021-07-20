package com.hy.zookeeper.config.service;

import java.util.List;
import java.util.Map;

import com.hy.zookeeper.config.entity.PlatformServerType;

/**
 * 服务联系
 * @author hrh
 *
 */
public interface IServerTypeService {
	
	/**
	 * 获取所有的服务类型
	 * @param page 页数
	 * @param 每页的数量
	 * @return
	 */
	Map<String,Object> getAllTypePage(int page, int row, PlatformServerType serverType);
	
	/**
	 * 获取没有注册的服务类型
	 * @param serverId 某个配置服务的id
	 * @return
	 */
	List<PlatformServerType> getNoUseType(String serverId);
	
	/**
	 * 根据id获取服务类型
	 * @param id
	 * @return
	 */
	PlatformServerType getTypeByID(String id);
	
	/**
	 * 删除服务类型
	 * @param id
	 * @return
	 */
	boolean deleteType(String id);
	
	/**
	 * 保存服务类型
	 * @param serverType
	 * @return
	 */
	boolean saveServerType(PlatformServerType serverType);
	
	/**
	 * 获取所有的类型
	 * @return
	 */
	List<PlatformServerType> getAllType();
	
	/**
	 * 
	 * @param serverType
	 * @return
	 */
	long getCount(PlatformServerType serverType);
	
	/**
	 * 设置service_type节点数据
	 */
	public void setServiceTypeNodeData();

}
