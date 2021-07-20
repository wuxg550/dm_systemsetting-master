package com.hy.zookeeper.config.controller;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.common.MockConst;
import com.hy.zookeeper.config.entity.PlatformServerType;
import com.hy.zookeeper.config.util.ObjectToValueMap;

public class ServerTypeControllerTest extends ControllerTestBase{
	
	private static final String uri = "/serverType";
	
	PlatformServerType serverType = new PlatformServerType();
	
	@Before
	public void setup(){
		serverType.setId("001ba9f6b82848b4b557775175990961");
		serverType.setServerTypeName("空调公共服务");
		serverType.setServerType("AirConditionerManage");
	}
	
	@Test
	public void getAll(){
		MvcResult result = sendRequest(uri+"/getAll", true, new LinkedMultiValueMap<>());
		assertServerTypeNotEmpty(result);
	}
	
	@Test
	public void getAllTypePage(){
		MultiValueMap<String, String> valueMap = ObjectToValueMap.getObjectValueMap(serverType);
		valueMap.add("page", "1");
		valueMap.add("rows", "5");
		MvcResult result = sendRequest(uri+"/getAllTypePage", false, valueMap);
		assertRows(result);
	}
	
	@Test
	public void getNoUseType(){
		MvcResult result = sendRequest(uri+"/getNoUseType", true, new LinkedMultiValueMap<>());
		assertServerTypeNotEmpty(result);
	}
	
	@Test
	public void saveType(){
		MvcResult result = sendRequest(uri+"/saveType", false, ObjectToValueMap.getObjectValueMap(serverType));
		assertReturnTrue(result);
	}
	
	@Test
	public void deletType(){
		MvcResult result = sendRequest(uri+"/deletType", false, ObjectToValueMap.getObjectValueMap(serverType));
		assertReturnTrue(result);
	}
	
	@Test
	public void exist(){
		MvcResult result = sendRequest(uri+"/exist", false, ObjectToValueMap.getObjectValueMap(serverType));
		Assert.assertFalse(MockConst.TRUE_STRING.equals(getContentAsString(result)));
	}
	
	private void assertServerTypeNotEmpty(MvcResult result){
		String content = getContentAsString(result);
		List<PlatformServerType> types = JSON.parseArray(content, PlatformServerType.class);
		Assert.assertFalse(types.isEmpty());
	}
	
	@Test
	public void updataServerType(){
		sendRequest(uri+"/updataServerType", true, ObjectToValueMap.getObjectValueMap(serverType));
	}
	
	@Test
	public void serverTypeList(){
		sendRequest(uri+"/serverTypeList", true, ObjectToValueMap.getObjectValueMap(serverType));
	}
	
	@Test
	public void serverTypeEdit(){
		sendRequest(uri+"/serverTypeEdit", true, ObjectToValueMap.getObjectValueMap(serverType));
	}
}
