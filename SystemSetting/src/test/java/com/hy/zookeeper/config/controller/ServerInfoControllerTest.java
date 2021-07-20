package com.hy.zookeeper.config.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.common.MockConst;
import com.hy.zookeeper.config.dto.CommonServerInfo;
import com.hy.zookeeper.config.dto.ResultDTO;
import com.hy.zookeeper.config.entity.RunningConfig;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.service.IServerInfoService;
import com.hy.zookeeper.config.util.ObjectToValueMap;
import com.hy.zookeeper.config.util.StringUtil;
import com.hy.zookeeper.config.util.UUIDTOOL;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerInfoControllerTest extends ControllerTestBase{

	private static String uri = "/serverInfo";
	private static String serverId = "518a8ed6a1034ac2b3559cfa74187adf";
	private static String serverType = "IPSubSystem";
	
	@Resource
	private IServerInfoService serverInfoService;
	
	private MultiValueMap<String, String> valueMap = null;
	
	ServerInfo serverInfo = new ServerInfo();
	
	@Before
	public void setup(){
		serverInfo.setId(serverId);
		serverInfo.setAddressType("IP");
		serverInfo.setServerType("IPSubSystem");
		serverInfo.setServerName("语音对讲子系统");
		serverInfo.setServerIp("127.0.0.1");
		
		valueMap = ObjectToValueMap.getObjectValueMap(serverInfo);
		
		valueMap.add("page", "1");
		valueMap.add("rows", "15");
		valueMap.add("cumlum", "status");
		valueMap.add("status", "1");
	}
	
	@Test 
	public void configServerList(){
		sendRequest(uri+"/configServerList", true, valueMap);
	}
	
	@Test 
	public void configServerEdit(){
		sendRequest(uri+"/configServerEdit", true, valueMap);
	}
	
	@Test
	public void getServerPage(){
		MvcResult result = sendRequest(uri+"/getServerPage", false, valueMap);
		String content = getContentAsString(result);
		Assert.assertTrue(StringUtils.isNoneBlank(content));
		JSONObject obj = JSON.parseObject(content);
		Assert.assertFalse(obj.isEmpty());
	}
	
	@Test
	public void saveServer(){
		sendRequest(uri+"/saveServer", false, valueMap);
		Assert.assertTrue(ZookeeperUtil.getCuratorClient().checkExist(
				getServerIdPath()+ZookeeperUtil.INFO));
		Assert.assertTrue(ZookeeperUtil.getCuratorClient().checkExist(
				getServerIdPath()+ZookeeperUtil.IP));
		
		ServerInfo serverInfoTest1 = new ServerInfo();
		serverInfoTest1.setServerType(UUIDTOOL.getuuid(32));
		assertReturnTrue(sendRequest(uri+"/saveServer", false, ObjectToValueMap.getObjectValueMap(serverInfoTest1)));
		
		serverInfoTest1.setId(UUIDTOOL.getuuid(32)+UUIDTOOL.getuuid(32)+UUIDTOOL.getuuid(32));
		assertReturnFalse(sendRequest(uri+"/saveServer", false, ObjectToValueMap.getObjectValueMap(serverInfoTest1)));
	}
	
	@Test
	public void updataServer(){
		sendRequest(uri+"/updataServer", true, valueMap);
	}
	
	@Test
	public void zdelServer(){
		serverInfoService.saveServer(serverInfo);
		sendRequest(uri+"/delServer", false, valueMap);
		Assert.assertFalse(ZookeeperUtil.getCuratorClient().checkExist(
				getServerIdPath()+ZookeeperUtil.INFO));
		Assert.assertFalse(ZookeeperUtil.getCuratorClient().checkExist(
				getServerIdPath()+ZookeeperUtil.IP));
	}
	
	@Test
	public void updServer(){
		serverInfoService.saveServer(serverInfo);
		String content = getContentAsString(sendRequest(uri+"/updServer", false, valueMap));
		Assert.assertTrue("1".equals(content));
	}
	
	@Test
	public void getServerSelectData(){
		String content = getContentAsString(sendRequest(uri+"/getServerSelectData", true, valueMap));
		Assert.assertTrue(StringUtil.isNoneBlank(content));
	}
	
	@Test
	public void getOneServers(){
		sendRequest(uri+"/getOneServer", true, valueMap);
	}
	
	@Test
	public void runningConfig(){
		serverInfoService.saveServer(serverInfo);
		sendRequest(uri+"/runningConfig", true, valueMap);
	}
	
	@Test
	public void getRunningConfig(){
		serverInfoService.saveServer(serverInfo);
		sendRequest(uri+"/getRunningConfig", false, valueMap);
	}
	
	@Test
	public void saveRunningConfig(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.putAll(this.valueMap);
		
		RunningConfig config = new RunningConfig();
		config.setCfgkey("123");
		config.setCfgname("123");
		config.setCfgval("123");
		List<RunningConfig> configs = new ArrayList<>();
		configs.add(config);
		
		valueMap.add("configData", JSON.toJSONString(configs));
		sendRequest(uri+"/saveRunningConfig", false, valueMap);
	}
	
	@Test
	public void commonServerInfo(){
		sendRequest(uri+"/commonServerInfo", true, valueMap);
	}
	
	@Test
	public void getServerDetail(){
		serverInfoService.saveServer(serverInfo);
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("serverId", serverId);
		sendRequest(uri+"/getServerDetail", true, valueMap);
	}
	
	@Test
	public void delOfflineServer(){
		String content = getContentAsString(deleteRequest(uri+"/delOfflineServer", valueMap));
		ResultDTO dto = JSON.parseObject(content, ResultDTO.class);
		Assert.assertTrue(dto.getSuccess());
	}
	
	@Test
	public void batchDelete(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		List<String> serverIds = new ArrayList<>();
		serverIds.add(serverId);
		valueMap.add("serverIds", JSON.toJSONString(serverIds));
		deleteRequest(uri+"/batchDelete", valueMap);
	}
	
	@Test
	public void getConfigFlagEnum(){
		sendRequest(uri+"/getConfigFlagEnum", true, valueMap);
	}
	
	@Test
	public void saveCommonServerInfo(){
		CommonServerInfo info = new CommonServerInfo();
		info.setOrgId("111111111111111111");
		info.setOrgName("测试机构");
		info.setCascadeDomain("192.168.2.10");
		
		MvcResult result = sendRequest(uri+"/saveCommonServerInfo", false
				, ObjectToValueMap.getObjectValueMap(info));
		assertSucess(result);
	}
	
	@Test
	public void getCommonServerInfo(){
		String content = getContentAsString(sendRequest(uri+"/getCommonServerInfo", false, valueMap));
		CommonServerInfo info = JSON.parseObject(content, CommonServerInfo.class);
		Assert.assertTrue(info!=null);
	}
	
	private String getServerIdPath(){
		return MockConst.DOMAIN+ZookeeperUtil.SERVERS
				+ZookeeperUtil.SEPARATOR+serverType
				+ZookeeperUtil.INSTANCE+ZookeeperUtil.SEPARATOR
				+serverId;
	}
	
	@Test
	public void getServer(){
		MvcResult result = sendRequest(uri+"/getServer", false, valueMap);
		String content = getContentAsString(result);
		Assert.assertTrue(StringUtils.isNoneBlank(content));
		JSONArray obj = JSON.parseArray(content);
		Assert.assertFalse(obj.isEmpty());
	}
}
