package com.hy.zookeeper.config.service;

import java.util.List;
import java.util.Map;

import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.dto.CommonServerInfo;
import com.hy.zookeeper.config.entity.RunningConfig;
import com.hy.zookeeper.config.entity.ServerInfo;


public interface IServerInfoService {
	
	/**
	 * 获取全部配置服务的信息
	 * @param page 也数
	 * @param rows 
	 * @return
	 */
	Map<String,Object> findAllPage(int page, int rows, ServerInfo info);
	
	/**
	 * 获取全部配置服务的信息
	 * @return
	 */
	List<ServerInfo> findAll();
	
	/**
	 * 保存配置信息
	 * @param serverInfo
	 * @return
	 */
	boolean saveServer(ServerInfo serverInfo);
	
	/**
	 * 根据id查看serverInfo
	 * @param id
	 * @return
	 */
	ServerInfo getServerById(String id);
	
	
	/**
	 * 根据id删除server
	 * @param id
	 * @return
	 */
	Map<String,Object> deleteServer(String id);
	
	
	/**
	 * 根据id更新状态
	 * @param id 
	 * @param status 原来的状态
	 * @param cumlum 更新的列名
	 * @return
	 */
	int updateStatus(String id, String status, String cumlum);
	
	// ----------2018/01/31  编辑--------------
	
	/**
	 * 获取服务信息下拉框数据
	 * @return
	 */
	public Map<String, Object> getServerSelectData();
	
	public List<RunningConfig> getRunningConfig(String serverId);
	
	public void saveRunningConfig(String configData, String serverId);
	
	/**
	 * 清理数据库中服务信息confing_falg!=1的数据
	 * 清理完将数据库中的服务注册到zookeeper服务
	 * @param exclusDelType  此参数已无用
	 * @param curator
	 */
	void cleanAndRegistServerInDb(String exclusDelType, CuratorClient curator);
	
	/**
	 * 获取级联域等公有的服务信息
	 * @return
	 */
	public CommonServerInfo getCommonServerInfo();
	
	/**
	 * 保存级联域等公有的服务信息
	 * @return
	 */
	public Map<String, Object> saveCommonServerInfo(CommonServerInfo info);
	
	/**
	 * 修改级联域等公有的服务信息
	 * @param commonInfo
	 */
	public void editServerInfo(CommonServerInfo commonInfo);
	
	Map<String, Object> getServerDatial(String serverId);
	
	/**
	 * 服务上下线处理
	 * @param serverId
	 * @param status
	 */
	public void serverOffline(String serverId, Integer status);
	
	/**
	 * 删除所有离线服务
	 * @return 返回删除服务的数量
	 */
	public int delOfflineServer();
	
	/**
	 * 根据id json串批量删除离线服务
	 * @param serverIds
	 * @return 返回删除服务的数量
	 */
	public int deleteServers(String serverIds);
	
	/**
	 * 1.将数据库中第三方服务数据同步至zookeeper节点；2.将zookeeper上非第三方服务的服务信息同步至数据库
	 */
	public void registeThirdPartyServer();
}
