package com.hy.zookeeper.config.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.TestBase;
import com.hy.zookeeper.config.common.RelationMockData;
import com.hy.zookeeper.config.dao.RelationsRepsotory;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.dto.AddressDto;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.entity.ServerRelation;
import com.hy.zookeeper.config.enums.OnlineStatusEnum;
import com.hy.zookeeper.config.enums.ResultKeyConst;
import com.hy.zookeeper.config.service.IServerInfoService;
import com.hy.zookeeper.config.service.IServerRelationService;

public class ServerRelationServiceImplTest  extends TestBase{

	@Resource
	private IServerRelationService relationService;
	
	@Resource
	private RelationsRepsotory relationsRepsotory;
	
	@Resource
	private ServerEntranceRepository entranceRepository;
	
	@Resource
	private IServerInfoService serverInfoService;
	
	private ServerRelation relation = null;
	
	@Before
	public void setup(){
		ServerEntrance entrance = entranceRepository.findByServerId(
				RelationMockData.mysqlServerId).get(0);
		relation = RelationMockData.getRelationMockData(entrance);
	}
	
	@Test
	public void ipChange(){
		relationService.saveRelation(relation);
		String ip = "192.168.0.1";
		AddressDto dto = new AddressDto();
		dto.setAddress(ip);
		dto.setAddressType("IP");
		relationService.ipChange(relation.getDestServerId(), JSON.toJSONString(dto));
		List<ServerRelation> relationList = relationsRepsotory.findByDestServerId(relation.getDestServerId());
		Assert.assertFalse(relationList.isEmpty());
		Assert.assertTrue(ip.equals(
				relationList.get(0).getDestIp()));
	}
	
	@Test
	public void updateRelation(){
		List<ServerRelation> relationList = new ArrayList<>();
		boolean result = relationService.updateRelation(relation.getSrcServerId(), relationList);
		Assert.assertTrue(result);
	}
	
	@Test
	public void serverOnOffLine(){
		relationService.saveRelation(relation);
		relationService.serverOnOffLine(relation.getDestServerId()
				, OnlineStatusEnum.OFFLINE.getStatus());
		List<ServerRelation> relationList = relationsRepsotory.findByDestServerId(relation.getDestServerId());
		Assert.assertFalse(relationList.isEmpty());
		Assert.assertTrue(OnlineStatusEnum.OFFLINE.getStatus().equals(
				relationList.get(0).getDestOnlineStatus()));
	}
	
	@Test
	public void deleteRelationByDestServerId(){
		relationService.saveRelation(relation);
		boolean result = relationService.deleteRelationByDestServerId(relation.getDestServerId());
		Assert.assertTrue(result);
	}
	
	@Test
	public void getServerIp(){
		relation.setId(null);
		ServerInfo serverInfo = serverInfoService.getServerById(relation.getDestServerId());
		serverInfo.setServerIp(null);
		serverInfoService.saveServer(serverInfo);
		relationService.saveRelation(relation);
		serverInfo.setServerIp(relation.getDestIp());
		serverInfoService.saveServer(serverInfo);
	}
	
	@Test
	public void getRelationPage(){
		ServerInfo srcServerInfo = serverInfoService.getServerById(relation.getSrcServerId());
		ServerInfo destServerInfo = serverInfoService.getServerById(relation.getDestServerId());
		relation.setSrcServerName(srcServerInfo.getServerName());
		relation.setSrcIp(srcServerInfo.getServerIp());
		relation.setDestServerName(destServerInfo.getServerName());
		relationService.saveRelation(relation);
		Map<String, Object> result = relationService.getRelationPage(1, 10, relation);
		Assert.assertTrue((long)result.get(ResultKeyConst.RECORDS_KEY) > 0);
	}
}
