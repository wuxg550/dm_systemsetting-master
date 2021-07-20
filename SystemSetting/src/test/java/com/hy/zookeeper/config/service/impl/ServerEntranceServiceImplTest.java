package com.hy.zookeeper.config.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hy.zookeeper.config.TestBase;
import com.hy.zookeeper.config.common.RelationMockData;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.dto.ServerEntranceDto;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.service.IServerEntranceService;

public class ServerEntranceServiceImplTest  extends TestBase{

	@Resource
	private IServerEntranceService entranceService;
	
	@Resource
	private ServerEntranceRepository entranceRepository;
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void getFcsMap(){
		Map<String, Object> result = entranceService.getFcsMap(RelationMockData.GISMAP_SERVERID);
		Assert.assertFalse(result.isEmpty());
	}
	
	@Test
	public void getEntrance(){
		Map<String, Object> result = entranceService.getEntrance(1, 15, null);
		Assert.assertFalse(result.isEmpty());
	}
	
	@Test
	public void getServerEntranceDtoById(){
		List<ServerEntrance> list = entranceRepository.findAll();
		if(!list.isEmpty()){
			ServerEntranceDto dto = entranceService.getServerEntranceDtoById(list.get(0).getId());
			Assert.assertTrue(dto != null);
		}
	}
}
