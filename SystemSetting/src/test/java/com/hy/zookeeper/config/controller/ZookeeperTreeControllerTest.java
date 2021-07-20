package com.hy.zookeeper.config.controller;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.common.MockConst;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZookeeperTreeControllerTest extends ControllerTestBase{

	private static final String uri = "/tree";
	
	@Value("${zookeeper.address}")
	private String zkAddress;
	
	@Test
	public void treeList(){
		sendRequest(uri+"/treeList", true, new LinkedMultiValueMap<>());
	}
	
	@Test
	public void getTree(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("ip", zkAddress.split(":")[0]);
		valueMap.add("port", zkAddress.split(":")[1]);
		
		// 获取基路径
		valueMap.remove("path");
		valueMap.add("path", "");
		sendRequest(uri+"/getTree", false, valueMap);
		
		// 获取/S4
		valueMap.remove("path");
		valueMap.add("path", MockConst.DOMAIN);
		sendRequest(uri+"/getTree", false, valueMap);
	}
	
	@Test
	public void getAllServer(){
		sendRequest(uri+"/getAllServer", false, new LinkedMultiValueMap<>());
	}
	
}
