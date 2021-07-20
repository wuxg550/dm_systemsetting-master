package com.hy.zookeeper.config.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.common.PageInfo;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.dto.EntranceDto;
import com.hy.zookeeper.config.dto.ServerEntranceDto;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.service.IServerEntranceService;
import com.hy.zookeeper.config.util.StringUtil;
import com.hy.zookeeper.config.util.UUIDTOOL;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@Transactional
@Service("serverEntranceService")
public class ServerEntranceServiceImpl implements IServerEntranceService{

	private static final Logger logger = LoggerFactory.getLogger(ServerEntranceServiceImpl.class);

	@Resource
	private ServerEntranceRepository entranceRepository;

	@Resource
	private ServerInfoRepsotory serverInfoRepository;

	@Value("${root.region}")
	private String region;

	@Override
	public List<ServerEntranceDto> getEntranceList(String serverType,
			String serverId) {

		List<ServerEntranceDto> serverEntranceDtoList = new ArrayList<>();

		List<ServerEntrance> serverEntranceList;
		if(StringUtil.isBlank(serverId)){
			serverEntranceList = entranceRepository.findAll();
		}else{
			serverEntranceList = entranceRepository.findByServerId(serverId);
		}
		if(serverEntranceList != null && !serverEntranceList.isEmpty()){
			for (ServerEntrance serverEntrance : serverEntranceList) {
				String fcJson = serverEntrance.getFcs();
				ServerInfo serverInfo = serverInfoRepository.findOne(serverEntrance.getServerId());
				if(StringUtil.isBlank(fcJson) || serverInfo == null){
					continue;
				}

				List<String> fcList = JSON.parseArray(fcJson, String.class);
				for (String string : fcList) {
					ServerEntranceDto serverEntranceDto = new ServerEntranceDto();
					serverEntranceDto.setId(serverEntrance.getId()+"_"+UUIDTOOL.getuuid(32));
					serverEntranceDto.setProtocol(serverEntrance.getProtocol());
					serverEntranceDto.setUrl(serverEntrance.getURL());
					serverEntranceDto.setPort(serverEntrance.getPort().toString());
					serverEntranceDto.setFCcode(string);
					serverEntranceDto.setServerId(serverEntrance.getServerId());

					serverEntranceDtoList.add(serverEntranceDto);
					serverEntranceDto.setServerName(serverInfo.getServerName());
					serverEntranceDto.setServerIp(serverInfo.getServerIp());
					serverEntranceDto.setServerType(serverInfo.getServerType());
				}
			}
		}

		return serverEntranceDtoList;
	}

	@Override
	public Map<String, Object> getFcsMap(String serverId) {
		Map<String, Object> result = new HashMap<>();
		List<ServerEntrance> list = entranceRepository.findByServerId(serverId);
		for(ServerEntrance l : list){
			List<String> fcs = JSON.parseArray(l.getFcs(), String.class);
			for(String fc : fcs){
				result.put(fc, l.getId());
			}
		}
		return result;
	}

	@Override
	public boolean saveEntrance(ServerEntrance serverEntrance) {
		if(StringUtils.isBlank(serverEntrance.getId())){
			serverEntrance.setId(UUIDTOOL.getuuid(32));
		}else{
			// 编辑入口，需比对与数据的功能码查看是否有删除
			ServerEntrance dbEntrance = entranceRepository.findOne(serverEntrance.getId());
			if(dbEntrance != null && StringUtil.isNotBlank(dbEntrance.getFcs())){
				List<String> fcs = JSON.parseArray(dbEntrance.getFcs(), String.class);
				fcs.removeAll(JSON.parseArray(serverEntrance.getFcs(), String.class));
				for(String fc : fcs){
					String fcIdPath = ZookeeperUtil.getFunctionCodesFcIdPath(fc, serverEntrance.getServerId());
					ZookeeperUtil.getCuratorClient().deleteNode(fcIdPath);
				}
			}
		}
		ServerEntrance newServerEntrance = entranceRepository.saveAndFlush(serverEntrance);
		if(newServerEntrance != null){
			String path = ZookeeperUtil.SEPARATOR+region+"/servers/"+newServerEntrance.getServerType()+"/instance/"+newServerEntrance.getServerId()+"/entrance";
			updateEntranceNode(newServerEntrance.getServerId(), path);
			return true;
		}else{
			return false;
		}

	}

	@Override
	public ServerEntrance findByEntranceId(String id) {
		return entranceRepository.findOne(id);
	}

	@Override
	public boolean delEntrance(String entranceId, String fc) {
		ServerEntrance serverEntrance = entranceRepository.findOne(entranceId);
		String path = ZookeeperUtil.SEPARATOR+region+"/servers/"+serverEntrance.getServerType()+"/instance/"+serverEntrance.getServerId()+"/entrance";
			try{
				entranceRepository.delete(entranceId);

				// 删除入口对应functionCodes子节点
				if(StringUtil.isNotBlank(serverEntrance.getFcs())){
					List<String> fcs = JSON.parseArray(serverEntrance.getFcs(), String.class);
					for(String f : fcs){
						String fcIdPath = ZookeeperUtil.getFunctionCodesFcIdPath(f
								, serverEntrance.getServerId());
						ZookeeperUtil.getCuratorClient().deleteNode(fcIdPath);
					}
				}

				updateEntranceNode(serverEntrance.getServerId(), path);
				return true;
			}catch(Exception e){
				logger.error("删除入口失败", e);
				return false;
			}
	}

	public void updateEntranceNode(String serverId, String path){
		CuratorClient curator = ZookeeperUtil.getCuratorClient();
		List<ServerEntrance> list = entranceRepository.findByServerId(serverId);
		List<EntranceDto> dtos = new ArrayList<>();
		for(ServerEntrance e : list){
			dtos.add(EntranceDto.getDto(e));
		}
		if(!curator.checkExist(path)){
			try {
				curator.createNode(path, JSON.toJSONString(dtos), CreateMode.PERSISTENT);
			} catch (Exception e1) {
				logger.error("创建入口节点失败，serverId:{}", serverId, e1);
			}
		}else{
			curator.updateNodeData(path, JSON.toJSONString(dtos));
		}
	}

	@Override
	public Map<String,Object> getEntrance(int page,int row,String serverId) {
		List<ServerEntranceDto> serverEntranceDtoList = new ArrayList<>();
		Map<String,Object> pageMap = new HashMap<>();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPage(page);
		pageInfo.setRows(row);
		String sql = "select * from BASISDATA1.platform_server_entrance";
		List<ServerEntrance> serverEntranceList;
		if(!org.springframework.util.StringUtils.isEmpty(serverId)){
			sql += "   where server_id ='"+serverId+"'";
			serverEntranceList = entranceRepository.findPageBySQL(sql, pageInfo);
		}else{
			serverEntranceList = entranceRepository.findPageBySQL(sql, pageInfo);
		}

		for (ServerEntrance serverEntrance : serverEntranceList) {
			ServerEntranceDto seDTO = new ServerEntranceDto();
			ServerInfo serverInfo = serverInfoRepository.findOne(serverEntrance.getServerId());
			if(serverInfo != null){
				seDTO.setServerIp(serverInfo.getServerIp());
				seDTO.setServerName(serverInfo.getServerName());
			}
			seDTO.setId(serverEntrance.getId());
			seDTO.setPort(serverEntrance.getPort()==null?"":serverEntrance.getPort().toString());
			seDTO.setProtocol(serverEntrance.getProtocol());
			seDTO.setServerType(serverEntrance.getServerType());
			seDTO.setUserName(serverEntrance.getUserName());
			seDTO.setUrl(serverEntrance.getURL());
			seDTO.setPassword(serverEntrance.getPassword());
			serverEntranceDtoList.add(seDTO);
		}
		pageMap.put("rows", serverEntranceDtoList);
		int total = (int) (pageInfo.getTotal()%row==0?pageInfo.getTotal()/row:pageInfo.getTotal()/row+1);
		pageMap.put("total", total);
		pageMap.put("records", pageInfo.getTotal());



		return pageMap;
	}

	@Override
	public ServerEntranceDto getServerEntranceDtoById(String serverEntranceId) {
		ServerEntranceDto seDTO = new ServerEntranceDto();
		seDTO.setFCcode("[]");
		if(!"".equals(serverEntranceId)){
			ServerEntrance se = entranceRepository.findOne(serverEntranceId);
			ServerInfo serverInfo = serverInfoRepository.findOne(se.getServerId());
			seDTO.setId(se.getId());
			seDTO.setPort(se.getPort()==null?"":se.getPort().toString());
			seDTO.setProtocol(se.getProtocol());
			seDTO.setServerIp(serverInfo.getServerIp());
			seDTO.setServerName(serverInfo.getServerName());
			seDTO.setServerType(se.getServerType());
			seDTO.setFCcode(se.getFcs());
			seDTO.setUrl(se.getURL());
			seDTO.setServerId(se.getServerId());
			seDTO.setUserName(se.getUserName());
			seDTO.setPassword(se.getPassword());}

		return seDTO;
	}
}
