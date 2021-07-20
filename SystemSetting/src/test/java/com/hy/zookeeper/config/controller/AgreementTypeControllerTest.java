package com.hy.zookeeper.config.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.common.MockConst;

public class AgreementTypeControllerTest extends ControllerTestBase{

	private static String uri = "/agreement";
	private static String id = "ee5c9cefff6c48fb8517c1d305ef8cd9";
	
	@Test
	public void indexsPage() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get(uri+"/agreementList")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("SESSIONNO", "")
				.content("") ;
		MvcResult result = mockMvc.perform(request).andReturn();
		int status = result.getResponse().getStatus();
		Assert.assertTrue("访问协议类型页失败", status == 200);
	}
	
	@Test
	public void editPage() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get(uri+"/editAgreementType")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("SESSIONNO", "")
				.param("id", id);
		MvcResult result = mockMvc.perform(request).andReturn();
		int status = result.getResponse().getStatus();
		Assert.assertTrue("访问协议类型页失败", status == 200);
	}
	
	@Test
	public void getAllType() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post(uri+"/getAllType")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("SESSIONNO", "")
				.content("") ;
		MvcResult result = mockMvc.perform(request).andReturn();
		int status = result.getResponse().getStatus();
		Assert.assertTrue("获取所有类型请求失败", status == 200);
	}
	
	@Test
	public void saveAgreement() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders.post(uri+"/saveAgreement")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("SESSIONNO", "")
				.param("id", "123321456654")
				.param("remarks", "保存测试")
				.param("type", "SaveTest");
		MvcResult result = mockMvc.perform(request).andReturn();
		int status = result.getResponse().getStatus();
		Assert.assertTrue("保存测试请求失败", status == 200);
		String content = result.getResponse().getContentAsString();
		Assert.assertTrue("删除测试失败", MockConst.TRUE_STRING.equals(content));
	}

	@Test
	public void delete() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders.post(uri+"/delete")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("SESSIONNO", "")
				.param("id", id);
		MvcResult result = mockMvc.perform(request).andReturn();
		int status = result.getResponse().getStatus();
		Assert.assertTrue("删除测试请求失败", status == 200);
		String content = result.getResponse().getContentAsString();
		Assert.assertTrue("删除测试失败", MockConst.TRUE_STRING.equals(content));
	}
	
	@Test
	public void getAllTypePage(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("page", "1");
		valueMap.add("rows", "15");
		
		MvcResult result = sendRequest(uri+"/getAllTypePage", false, valueMap);
		assertRows(result);
	}
	
}
