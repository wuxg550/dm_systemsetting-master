package com.hy.zookeeper.config.service;

import java.util.List;
import java.util.Map;

import com.hy.zookeeper.config.dto.ServerEntranceDto;
import com.hy.zookeeper.config.entity.ServerEntrance;

/**
 * 服务入口业务层
 * @author jianweng
 *
 */
public interface IServerEntranceService {

	/**
	 * 入口列表
	 * @param serverType
	 * @param serverId
	 * @return
	 */
	public List<ServerEntranceDto> getEntranceList(String serverType, String serverId);
	
	/**
	 * 获取功能码集合   fc-entranceId
	 * @param serverId
	 * @return
	 */
	public Map<String, Object> getFcsMap(String serverId);
	
	/**
	 * 保存服务入口
	 * @param serverEntrance
	 * @return
	 */
	public boolean saveEntrance(ServerEntrance serverEntrance);
	
	/**
	 * 获取服务入口的信息
	 * @param id entrance id
	 * @return
	 */
	public ServerEntrance findByEntranceId(String id);
	
	/**
	 * 根据入口id和功能码删除相对应的信息
	 * @param entranceId
	 * @param FC
	 * @return
	 */
	public boolean delEntrance(String entranceId, String fc);
	
	
	/**
	 * 获取服务入口列表
	 * @page 当前页
	 * @row 页数
	 * @return
	 */
	public Map<String,Object> getEntrance(int page, int row, String serverId);
	
	/**
	 * 根据入口id获取ServerEntranceDto
	 * @param serverEntranceId 入口ID
	 * @return
	 */
	public ServerEntranceDto getServerEntranceDtoById(String serverEntranceId);
}
