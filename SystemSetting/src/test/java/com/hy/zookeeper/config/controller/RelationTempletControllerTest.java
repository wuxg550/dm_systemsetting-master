package com.hy.zookeeper.config.controller;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.entity.RelationTemplet;
import com.hy.zookeeper.config.util.ObjectToValueMap;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RelationTempletControllerTest extends ControllerTestBase{

	private static final String uri = "/relation/templet";
	
	RelationTemplet templet = new RelationTemplet();
	
	@Before
	public void setup(){
		templet.setSrcServerType("DataTransformerService");
		templet.setSrcConsumerFc("FC_XT_UPDATE_CFGDATA_0001");
		templet.setDestServerType("BaseDataManage");
		templet.setDestProviderFc("FC_XT_UPDATE_CFGDATA_0001");
	}
	
	@Test
	public void list(){
		MvcResult result = sendRequest(uri+"/list", true, ObjectToValueMap.getObjectValueMap(templet));
		assertRows(result);
	}
	
	@Test
	public void save(){
		MvcResult result = sendRequest(uri+"/save", true, ObjectToValueMap.getObjectValueMap(templet));
		assertSucess(result);
	}
	
	@Test
	public void delete(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("id", "6582c229f39143d49e28aa99d7f8ee0f");
		MvcResult result = sendRequest(uri+"/delete", true, valueMap);
		assertSucess(result);
	}
	
	@Test
	public void checkExist(){
		MvcResult result = sendRequest(uri+"/checkExist", true, ObjectToValueMap.getObjectValueMap(templet));
		String content = getContentAsString(result);
		JSONObject object = JSON.parseObject(content);
		Assert.assertTrue(object.getBoolean("exist"));
	}
	
	@Test
	public void uploadExcel(){
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		File xmlFile = new File(path+"static/流向模板导入文件.xls");
		MvcResult result = null;
		try (FileInputStream is = new FileInputStream(xmlFile)){
			MockMultipartFile file = new MockMultipartFile("file", is);
			result = mockMvc.perform(MockMvcRequestBuilders.fileUpload(uri+"/uploadExcel").file(file)).andReturn();
		} catch (Exception e) {
			Assert.fail("上传文件失败");
		}
		Assert.assertTrue(result.getResponse().getStatus() == 200);
		assertSucess(result);
	}
	
	@Test
	public void downloadExcelTemplet(){
		sendRequest(uri+"/downloadExcelTemplet", true, ObjectToValueMap.getObjectValueMap(templet));
	}
	
	@Test
	public void downloadExcel(){
		sendRequest(uri+"/downloadExcel", true, ObjectToValueMap.getObjectValueMap(templet));
	}
}
