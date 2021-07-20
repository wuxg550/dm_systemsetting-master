package com.hy.zookeeper.config.service.impl;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.hy.zookeeper.config.TestBase;
import com.hy.zookeeper.config.service.IFunctionCodesService;
import com.hy.zookeeper.config.util.ZookeeperUtil;

public class FunctionCodesServiceImplTest extends TestBase{

	@Resource
	private IFunctionCodesService functionCodesService;
	
	private String serverId = "634f37c1649d4b85b0c61e74f80d096d";
	private String fc = "FC_GET_MYSQL_CONNECTIONSTRING_0001";
	private String entranceNodeData = "[{\"fcs\":[],\"id\":\"d22ee37427144bf4894e6594bb163755\",\"password\":\"123456\",\"port\":3306,\"protocol\":\"MYSQL\",\"url\":\"basisdata\",\"userName\":\"root\"}]"; 
	
	@Test
	public void updateFunctionCodes(){
		String fcPath = ZookeeperUtil.getFunctionCodesFcIdPath(fc, serverId);
		ZookeeperUtil.getCuratorClient().deleteNode(fcPath);
		functionCodesService.updateFunctionCodes(serverId);
		Assert.assertTrue(ZookeeperUtil.getCuratorClient().checkExist(fcPath));
	}
	
	@Test
	public void compareFcsAndUpdateFunctionCodes(){
		functionCodesService.compareFcsAndUpdateFunctionCodes(serverId, entranceNodeData);
		Assert.assertFalse(ZookeeperUtil.getCuratorClient().checkExist(
				ZookeeperUtil.getFunctionCodesFcIdPath(fc, serverId)));
	}
}
