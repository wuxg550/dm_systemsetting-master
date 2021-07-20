package com.hy.zookeeper.config.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.dto.EntranceDto;
import com.hy.zookeeper.config.dto.RelationDTO;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.enums.OnlineStatusEnum;
import com.hy.zookeeper.config.service.IFunctionCodesService;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@Service("functionCodesService")
public class FunctionCodesServiceImpl implements IFunctionCodesService {

	@Resource
	private ServerEntranceRepository entranceRepository;

	@Resource
	private ServerInfoRepsotory serverInfoRepsotory;

	@Override
	public void updateFunctionCodes(String serverId){
		List<ServerEntrance> entrances = entranceRepository.findByServerId(serverId);
		ServerInfo serverInfo = serverInfoRepsotory.findOne(serverId);
		for(ServerEntrance e : entrances){
			for(String fc : e.getFcList()){
				createOrUpdateFunctionCodesNode(serverInfo, fc, e);
			}
		}
	}

	@Override
	public void compareFcsAndUpdateFunctionCodes(String serverId, String entranceNodeData){
		// 功能码-入口DTO集合
		Map<String, EntranceDto> fcEntranceMap = EntranceDto.getEntranceDtoFcMap(entranceNodeData);

		ServerInfo serverInfo = serverInfoRepsotory.findOne(serverId);
		for(Entry<String, EntranceDto> e : fcEntranceMap.entrySet()){
			String fc = e.getKey();
			RelationDTO dto = buildFunctionCodesDOT(serverInfo, e.getValue(), fc);
			createOrUpdateFunctionCodesNode(serverInfo, fc, dto);
		}

		List<ServerEntrance> entrances = entranceRepository.findByServerId(serverId);
		// 无入口数据直接返回
		if(entrances == null){
			return;
		}

		CuratorClient client = ZookeeperUtil.getCuratorClient();
		for(ServerEntrance e : entrances){
			for(String fc : e.getFcList()){
				// 功能码已删除的则删除对应的FunctionCodes子节点
				if(fcEntranceMap.get(fc) == null){
					String fcPath = ZookeeperUtil.getFunctionCodesFcIdPath(fc, serverId);
					client.deleteNode(fcPath);
				}
			}
		}
	}

	protected void createOrUpdateFunctionCodesNode(ServerInfo serverInfo, String fc, ServerEntrance entrance){
		RelationDTO dto = buildFunctionCodesDOT(serverInfo, entrance, fc);
		createOrUpdateFunctionCodesNode(serverInfo, fc, dto);
	}

	protected void createOrUpdateFunctionCodesNode(ServerInfo serverInfo, String fc, RelationDTO dto){
		if(serverInfo == null){
			return;
		}
		String fcPath = ZookeeperUtil.getFunctionCodesFcIdPath(fc, serverInfo.getId());
		// 服务在线，更新节点
		if(OnlineStatusEnum.ONLINE.getStatus().equals(serverInfo.getOnlineStatus())){
			ZookeeperUtil.getCuratorClient()
			    .createOrUpdatePath(fcPath, JSON.toJSONString(dto));
		}else{
			// 服务离线，删除节点
			ZookeeperUtil.getCuratorClient().deleteNode(fcPath);
		}
	}

	protected RelationDTO buildFunctionCodesDOT(ServerInfo serverInfo){
		RelationDTO dto = new RelationDTO();
		if(serverInfo == null){
			return dto;
		}
		dto.setDestIp(serverInfo.getServerIp());
		dto.setDestServerId(serverInfo.getId());
		dto.setDestServerType(serverInfo.getServerType());
		return dto;
	}

	protected RelationDTO buildFunctionCodesDOT(ServerInfo serverInfo, ServerEntrance entrance, String fc){
		RelationDTO dto = buildFunctionCodesDOT(serverInfo);
		dto.setDestEntranceId(entrance.getId());
		dto.setDestPort(entrance.getPort());
		dto.setDestProtocol(entrance.getProtocol());
		dto.setDestProviderFc(fc);
		dto.setDestUrl(entrance.getURL());
		dto.setUserName(entrance.getUserName());
		dto.setPassword(entrance.getPassword());
		return dto;
	}

	protected RelationDTO buildFunctionCodesDOT(ServerInfo serverInfo, EntranceDto entrance, String fc){
		RelationDTO dto = buildFunctionCodesDOT(serverInfo);
		dto.setDestPort(entrance.getPort());
		dto.setDestProtocol(entrance.getProtocol());
		dto.setDestProviderFc(fc);
		dto.setDestUrl(entrance.getUrl());
		dto.setUserName(entrance.getUserName());
		dto.setPassword(entrance.getPassword());
		return dto;
	}

	@Override
	public void validateFunctionCodes() {

		Map<String, ServerInfo> serverInfoMap = getServerInfoMap();

		List<ServerEntrance> entrances = entranceRepository.findAll();
		for(ServerEntrance e : entrances){
			for(String fc : e.getFcList()){
				createOrUpdateFunctionCodesNode(serverInfoMap.get(e.getServerId()), fc, e);
			}
		}

	}

	protected Map<String, ServerInfo> getServerInfoMap(){
		Map<String, ServerInfo> map = new HashMap<>();
		List<ServerInfo> list = serverInfoRepsotory.findAll();
		if(list != null){
			for(ServerInfo s : list){
				map.put(s.getId(), s);
			}
		}
		return map;
	}

}
