package com.hy.zookeeper.config.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.common.MockConst;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.util.ObjectToValueMap;
import com.hy.zookeeper.config.util.StringUtil;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntranceControllerTest extends ControllerTestBase{

	private static String uri = "/entrance";
	
	private ServerEntrance entrance = new ServerEntrance();
	
	private MultiValueMap<String, String> valueMap = null;
	
	@Resource
	private ServerEntranceRepository entranceRepository;
	
	@Before
	public void setup(){
		entrance.setId("5824277bdf764455a008174d939b37dd");
		entrance.setPort(20190);
		entrance.setFcs("[\"get01\",\"get02\"]");
		entrance.setUserName("admin");
		entrance.setPassword("123456");
		entrance.setProtocol("TCP");
		entrance.setServerId("74ac48e6a26347cf93997f77b061578f");
		entrance.setServerType("CSGateway");
		entrance.setURL("");
		
		valueMap = ObjectToValueMap.getObjectValueMap(entrance);
	}
	
	@Test 
	public void entranceList(){
		sendRequest(uri+"/entranceList", true, valueMap);
	}
	
	@Test 
	public void entranceEdit(){
		sendRequest(uri+"/entranceEdit", true, valueMap);
	}
	
	@Test
	public void getTableData(){
		MvcResult result = sendRequest(uri+"/getTableData", false, valueMap);
		String content = getContentAsString(result);
		Assert.assertTrue(StringUtils.isNoneBlank(content));
	}
	
	@Test
	public void saveEntrance(){
		MvcResult result = sendRequest(uri+"/saveEntrance", false, valueMap);
		String content = getContentAsString(result);
		Assert.assertTrue(MockConst.TRUE_STRING.equals(content));
	}
	
	@Test
	public void zdelEntrance(){
		entranceRepository.save(entrance);
		valueMap.add("entranceId", entrance.getId());
		sendRequest(uri+"/delEntrance", false, valueMap);
		// 断言删除zookeeper节点
		Assert.assertFalse(ZookeeperUtil.getCuratorClient().checkExist(getEntrancePath(entrance)));
	}
	
	@Test
	public void getEntranceList(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.putAll(this.valueMap);
		valueMap.add("page", "1");
		valueMap.add("rows", "15");
		valueMap.add("serverId", "caf5acb030714833af7760b521eb5bf9");
		MvcResult result = sendRequest(uri+"/getEntranceList", false, valueMap);
		String content = getContentAsString(result);
		Assert.assertTrue(StringUtil.isNoneBlank(content));
		JSONObject object = JSON.parseObject(content);
		Assert.assertTrue(StringUtil.isNoneBlank(object.getString(MockConst.TOTAL_KEY)));
	}
	
	@Test
	public void serverEntranceEdit(){
		sendRequest(uri+"/entranceList", true, valueMap);
	}
	
	
	@Test
	public void getFcs(){
		MvcResult result = sendRequest(uri+"/entranceList", true, valueMap);
		String content = getContentAsString(result);
		Assert.assertTrue(StringUtil.isNoneBlank(content));
	}
	
	
	private String getEntrancePath(ServerEntrance entrance){
		return MockConst.DOMAIN+ZookeeperUtil.SERVERS
				+ZookeeperUtil.SEPARATOR+entrance.getServerType()
				+ZookeeperUtil.INSTANCE+ZookeeperUtil.SEPARATOR
				+entrance.getId()+ZookeeperUtil.ENTRANCE;
	}
	
}
