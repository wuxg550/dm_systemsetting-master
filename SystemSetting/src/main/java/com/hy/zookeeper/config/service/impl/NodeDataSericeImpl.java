package com.hy.zookeeper.config.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.common.NodeData;
import com.hy.zookeeper.config.common.ResultRuok;
import com.hy.zookeeper.config.common.ResultStat;
import com.hy.zookeeper.config.common.ZkNode;
import com.hy.zookeeper.config.dao.RelationsRepsotory;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.dto.AddressDto;
import com.hy.zookeeper.config.dto.EntranceDto;
import com.hy.zookeeper.config.dto.ServerInfoDto;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.entity.ServerRelation;
import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.enums.ConfigFlagEnum;
import com.hy.zookeeper.config.enums.OnlineStatusEnum;
import com.hy.zookeeper.config.enums.ServerStatusEnum;
import com.hy.zookeeper.config.service.INodeDataService;
import com.hy.zookeeper.config.util.UUIDTOOL;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@Service
@Transactional(value="jpaTransactionManager")
public class NodeDataSericeImpl implements INodeDataService {

	private static final Logger logger = LoggerFactory.getLogger(NodeDataSericeImpl.class);

	@Value("${root.region}")
	private String region;

	@Value("${zookeeper.address}")
	private String zkConnectString;

	@Resource
	private ServerInfoRepsotory serverInfoRepsotory;

	@Resource
	private RelationsRepsotory relationsRepsotory;

	@Resource
	private ServerEntranceRepository entranceRepository;

	@Override
	public List<Zkserver> findAllZkServer() {
		List<Zkserver> serverList = new ArrayList<>();
		if(StringUtils.isNotBlank(zkConnectString)){
			zkConnectString = zkConnectString.trim();
			for(String s : zkConnectString.split(",")){
				Zkserver zk = new Zkserver();
				zk.setIp(s.split(":")[0]);
				zk.setPort(s.split(":")[1]);
				serverList.add(zk);
			}
		}
		for (Zkserver info : serverList) {
			ResultStat resultStat = ZookeeperUtil.stat(info);
			if (resultStat != null) {
				ResultRuok ruok  = ZookeeperUtil.ruok(info);
				if (ruok == null) {
					info.setStatus(ServerStatusEnum.OFFLINE.toString());
					continue;
				}
				if (StringUtils.isNotBlank(ruok.getImok())) {
					info.setStatus(ServerStatusEnum.ONLINE.toString());
				}else{
					info.setStatus(ServerStatusEnum.EXCEPTIOM.toString());
				}
			}else{
				info.setStatus(ServerStatusEnum.OFFLINE.toString());
			}
		}
		return serverList;
	}

	@Override
	public List<ZkNode> getNodeDataList(String id,String host) {
		if(!StringUtils.isNotBlank(id) || !StringUtils.isNotBlank(host)){
			return new ArrayList<>();
		}
		List<ZkNode> childrenList = new ArrayList<>();
		try {
			List<String> childrenNode = ZookeeperUtil.getCuratorClient().getClildrenList(id);
			if (childrenNode != null) {
				for (String node : childrenNode) {
					ZkNode zkNode = new ZkNode();
					zkNode.setId(node);
					zkNode.setpId(id);
					String fullPath = "";
					if ("/".equals(id)) {
						fullPath = id+node;
					}else{
						fullPath = id+ZookeeperUtil.SEPARATOR+node;
					}
					zkNode.setFullId(fullPath);

					NodeData nodeData = ZookeeperUtil.getCuratorClient().getForNodeData(zkNode.getFullId());
					zkNode.setData(nodeData.getData());
					zkNode.setNodeModel(nodeData.getNodeModel());
					childrenList.add(zkNode);
				}
			}
		} catch (Exception e) {
			logger.error("获取节点列表异常", e);
		}
		return childrenList;
	}

	@Override
	public void syncNodeData() {
		CuratorClient client = ZookeeperUtil.getCuratorClient();
		Map<String, String> pathMap = new HashMap<>();
		String basePath = ZookeeperUtil.SEPARATOR + region + "/servers";
		List<String> serverTypes = client.getClildrenList(basePath);
		serverTypes.forEach(t -> {
			String path = ZookeeperUtil.SEPARATOR + region + "/servers/" + t + "/instance";
			pathMap.put(t, path);
		});

		List<ServerInfo> saveInfos = new ArrayList<>();
		List<ServerEntrance> saveEntrances = new ArrayList<>();
		List<ServerRelation> saveRealtions = new ArrayList<>();

		for(Entry<String, String> entry : pathMap.entrySet()){
			String path = pathMap.get(entry.getKey());
			if(!client.checkExist(path)){
				continue;
			}
			List<String> children = client.getClildrenList(path);
			for(String c : children){
				ServerInfo info = new ServerInfo();
				info.setId(c);
				info.setServerType(entry.getKey());
				info.setDomain(region);
				try{
					syncServerInfo(info, client, path);

					syncAddressInfo(info, client, path);

					syncOnlineInfo(info, client, path);

					saveInfos.add(info);

					syncEntranceInfo(info, client, path, saveEntrances);

					syncRelationInfo(info, client, path, saveRealtions);
				}catch(Exception e){
					logger.error("同步节点异常，服务ID:{}", c, e);
				}
			}
		}

		List<String> registerServerIds = Lists.newArrayList();

		saveInfos.forEach(i -> {
			if(!i.isRemoteServer()){
				registerServerIds.add(i.getId());
			}
		});

		serverInfoRepsotory.deleteByConfingFalgNot(ConfigFlagEnum.REMOTE.getFlag());
		if(!saveInfos.isEmpty()){
			serverInfoRepsotory.save(saveInfos);
		}

		// 只删除非第三方服务的入口
		entranceRepository.deleteByServerIdIn(registerServerIds);
		if(!saveEntrances.isEmpty()){
			entranceRepository.save(saveEntrances);
		}

		relationsRepsotory.deleteAll();
		if(!saveRealtions.isEmpty()){
			for(ServerRelation r : saveRealtions){
				r.setId(UUIDTOOL.getuuid(32));
			}
			relationsRepsotory.save(saveRealtions);
		}

	}

	@Override
	public void deleteNode(String path) {
		CuratorFramework client = ZookeeperUtil.getCuratorClient().getClient();
		try {
			recurDeleteNode(path, path, client);
		} catch (Exception e) {
			logger.error("删除节点异常:{}", path, e);
		}
	}

	public void  recurDeleteNode(String path, String childPath, CuratorFramework client) throws Exception{
		List<String> children = client.getChildren().forPath(childPath);
		if(!CollectionUtils.isEmpty(children)){
			for(String c : children){
				String cPath = childPath + ZookeeperUtil.SEPARATOR + c;
				recurDeleteNode(path, cPath, client);
			}
		}
		client.delete().forPath(childPath);
	}

	private void syncServerInfo(ServerInfo info, CuratorClient client, String basePath){
		// 读取info节点
		String infoPath = basePath + ZookeeperUtil.SEPARATOR + info.getId() + ZookeeperUtil.INFO ;
		if(client.checkExist(infoPath)){
			String data = client.getNodeData(infoPath);
			if(StringUtils.isNotBlank(data)){
				ServerInfoDto dto = JSON.parseObject(data, ServerInfoDto.class);
				info.setServerName(dto.getServerName());
				info.setOrgCode(dto.getOrgCode());
				info.setVersion(dto.getVersion());
				info.setCascadeDomain(dto.getCascadeDomain());
				info.setOrgId(dto.getOrgId());
				info.setOrgName(dto.getOrgName());
			}
		}
	}

	private void syncAddressInfo(ServerInfo info, CuratorClient client, String basePath){
		// 读取ip节点
		String ipPath = basePath + ZookeeperUtil.SEPARATOR + info.getId() + ZookeeperUtil.IP;
		if(client.checkExist(ipPath)){
			String data = client.getNodeData(ipPath);
			if(StringUtils.isNotBlank(data)){
				AddressDto dto = JSON.parseObject(data, AddressDto.class);
				if(dto != null){
					info.setServerIp(dto.getAddress());
					info.setAddressType(dto.getAddressType());
				}

			}
		}
	}

	private void syncOnlineInfo(ServerInfo info, CuratorClient client, String basePath){
		String onlinePath = basePath + ZookeeperUtil.SEPARATOR + info.getId() + ZookeeperUtil.ONLINE;
		info.setConfingFalg("2");
		if(client.checkExist(onlinePath)){
			if(ZookeeperUtil.ONLINEDATA.equals(client.getNodeData(onlinePath))){
				info.setConfingFalg("1");
			}
			info.setOnlineStatus(OnlineStatusEnum.ONLINE.getStatus());
		}else{
			info.setOnlineStatus(OnlineStatusEnum.OFFLINE.getStatus());
		}
	}

	private void syncEntranceInfo(ServerInfo info, CuratorClient client, String basePath, List<ServerEntrance> saveEntrances){
		// 第三方服务的入口不需要读取
		if(!info.isRemoteServer()){
			// 读取entrance节点
			String entrancePath = basePath + ZookeeperUtil.SEPARATOR + info.getId() + ZookeeperUtil.ENTRANCE;
			if(client.checkExist(entrancePath)){
				String data = client.getNodeData(entrancePath);
				if(StringUtils.isNotBlank(data)){
					List<EntranceDto> dtos = JSON.parseArray(data, EntranceDto.class);
					for(EntranceDto dto : dtos){
						ServerEntrance entrance = new ServerEntrance(info.getServerType(), info.getId(), dto);
						saveEntrances.add(entrance);
					}
				}
			}
		}
	}

	private void syncRelationInfo(ServerInfo info, CuratorClient client, String basePath, List<ServerRelation> saveRealtions){
		// 读取relation节点
		String relationPath = basePath + ZookeeperUtil.SEPARATOR + info.getId() + ZookeeperUtil.RELATION;
		if(client.checkExist(relationPath)){
			String data = client.getNodeData(relationPath);
			if(StringUtils.isNotBlank(data)){
				List<ServerRelation> relations = JSON.parseArray(data, ServerRelation.class);
				saveRealtions.addAll(relations);
			}
		}
	}

}
