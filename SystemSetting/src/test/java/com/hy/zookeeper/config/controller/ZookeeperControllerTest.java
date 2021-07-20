package com.hy.zookeeper.config.controller;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.util.ObjectToValueMap;
import com.hy.zookeeper.config.util.UUIDTOOL;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZookeeperControllerTest extends ControllerTestBase{

	@Test
	public void root(){
		sendRequest("/", true, new LinkedMultiValueMap<>());
	}
	
	@Test
	public void index(){
		sendRequest("/index", true, new LinkedMultiValueMap<>());
	}
	
	@Test
	public void serverList(){
		sendRequest("/serverList", true, new LinkedMultiValueMap<>());
	}
	
	@Test
	public void serverEdit(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("id", "1111111111111111");
		sendRequest("/serverEdit", true, valueMap);
	}
	
	@Test
	public void getAllPage(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("page", "1");
		valueMap.add("rows", "15");
		sendRequest("/getAllPage", false, valueMap);
	}
	
	@Test
	public void initNode(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("ip", getZkConnectString().split(":")[0]);
		valueMap.add("port", getZkConnectString().split(":")[1]);
		sendRequest("/initNode", true, valueMap);
	}
	
	@Test
	public void saveServer(){
		Zkserver zkserver = new Zkserver();
		zkserver.setId(UUIDTOOL.getuuid(32));
		zkserver.setIp("127.0.0.1");
		zkserver.setPort("2181");
		MultiValueMap<String, String> valueMap = ObjectToValueMap.getObjectValueMap(zkserver);
		sendRequest("/saveServer", true, valueMap);
	}
	
	@Test
	public void delServer(){
		sendRequest("/delServer/"+UUIDTOOL.getuuid(32), true, new LinkedMultiValueMap<>());
	}
}
