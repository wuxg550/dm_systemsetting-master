package com.hy.zookeeper.config;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.zookeeper.config.common.MockConst;
import com.hy.zookeeper.config.enums.ResultKeyConst;

public class ControllerTestBase extends TestBase{

	protected MockMvc mockMvc; // 模拟MVC对象
	@Autowired  
    private WebApplicationContext wac; // 注入WebApplicationContext
	
	@Before
    public void setupBase() {  
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();  
    }
	
	/**
	 * 发送get/post请求
	 * @param url
	 * @param getRequest true为get请求，false为post请求
	 * @param valueMap 参数集
	 * @return 返回请求结果
	 */
	protected MvcResult sendRequest(String url, boolean getRequest
			, MultiValueMap<String, String> valueMap){
		RequestBuilder request = null;
		if(getRequest){
			request = MockMvcRequestBuilders.get(url)
					.header("SESSIONNO", "")
					.params(valueMap);
		}else{
			request = MockMvcRequestBuilders.post(url)
					.header("SESSIONNO", "")
					.params(valueMap);
		}
		
		String failMsg = url+"请求失败";
		MvcResult result = null;
		try {
			result = mockMvc.perform(request).andReturn();
		} catch (Exception e) {
			Assert.fail(failMsg);
		}
		int status = result.getResponse().getStatus();
		Assert.assertTrue(failMsg, status == 200);
		
		return result;
	}
	
	/**
	 * delete请求操作
	 * @param url
	 * @param valueMap
	 * @return
	 */
	protected MvcResult deleteRequest(String url, MultiValueMap<String, String> valueMap){
		RequestBuilder request = MockMvcRequestBuilders.delete(url)
				.header("SESSIONNO", "")
				.params(valueMap);
		String failMsg = url+"请求失败";
		MvcResult result = null;
		try {
			result = mockMvc.perform(request).andReturn();
		} catch (Exception e) {
			Assert.fail(failMsg);
		}
		int status = result.getResponse().getStatus();
		Assert.assertTrue(failMsg, status == 200);
		
		return result;
	}
	
	/**
	 * 获取返回内容
	 * @param result
	 * @return
	 */
	protected String getContentAsString(MvcResult result){
		String content = null;
		try {
			content = result.getResponse().getContentAsString();
		} catch (UnsupportedEncodingException e) {
			Assert.fail("内容为空");
		}
		return content;
	}
	
	/**
	 * 断言请求返回成功
	 * @param result
	 * @return
	 */
	protected void assertSucess(MvcResult result){
		try {
			String content = result.getResponse().getContentAsString();
			JSONObject obj = JSON.parseObject(content);
			Assert.assertTrue(StringUtils.isNoneBlank(obj.getString(ResultKeyConst.SUCESS_KEY)));
			Assert.assertTrue(MockConst.TRUE_STRING.equals(obj.getString(ResultKeyConst.SUCESS_KEY)));
		} catch (UnsupportedEncodingException e) {
			Assert.fail("内容为空");
		}
	}
	
	/**
	 * 断言请求返回rows数据
	 * @param result
	 * @return
	 */
	protected void assertRows(MvcResult result){
		try {
			String content = result.getResponse().getContentAsString();
			JSONObject obj = JSON.parseObject(content);
			String rowsStr = obj.getString(ResultKeyConst.ROWS_KEY);
			Assert.assertTrue(StringUtils.isNoneBlank(rowsStr));
		} catch (UnsupportedEncodingException e) {
			Assert.fail("内容为空");
		}
	}
	
	protected void assertReturnTrue(MvcResult result){
		try {
			String content = result.getResponse().getContentAsString();
			Assert.assertTrue(MockConst.TRUE_STRING.equals(content));
		} catch (UnsupportedEncodingException e) {
			Assert.fail("内容为空");
		}
	}
	
	protected void assertReturnFalse(MvcResult result){
		try {
			String content = result.getResponse().getContentAsString();
			Assert.assertTrue(MockConst.FALSE_STRING.equals(content));
		} catch (UnsupportedEncodingException e) {
			Assert.fail("内容为空");
		}
	}
}
