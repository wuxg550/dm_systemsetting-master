package com.hy.zookeeper.config.controller;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.common.MockConst;
import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NodeDataControllerTest extends ControllerTestBase{

	private static final String uri = "/node";
	
	@Value("${zookeeper.address}")
	private String zookeeperAddress;
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void index(){
		sendRequest(uri+"/nodeList", true, new LinkedMultiValueMap<>());
	}
	
	@Test
	public void findAllZkServer(){
		MvcResult result = sendRequest(uri+"/findAllZkServer", true, new LinkedMultiValueMap<>());
	    String content = getContentAsString(result);
	    List<Zkserver> zkServers = JSON.parseArray(content, Zkserver.class);
	    Assert.assertFalse(zkServers.isEmpty());
	}
	
	@Test
	public void getNodeDataList(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("node", MockConst.DOMAIN);
		valueMap.add("host", zookeeperAddress);
		MvcResult result = sendRequest(uri+"/getNodeDataList", true, valueMap);
		assertRows(result);
	}
	
	@Test
	public void validate(){
		MvcResult result = sendRequest(uri+"/validate", true, new LinkedMultiValueMap<>());
		assertSucess(result);
	}
	
	@Test
	public void delete(){
		String testPath = "/S4/TEST_PATH";
		try {
			ZookeeperUtil.getCuratorClient().createNode(testPath, "", CreateMode.PERSISTENT);
		} catch (Exception e) {
			Assert.fail("创建节点失败");
		}
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("path", testPath);
		MvcResult result = sendRequest(uri+"/delete", true, valueMap);
		assertSucess(result);
	}
}
