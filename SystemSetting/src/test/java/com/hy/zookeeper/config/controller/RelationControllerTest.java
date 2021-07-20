package com.hy.zookeeper.config.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.annotation.Resource;

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
import com.hy.zookeeper.config.ControllerTestBase;
import com.hy.zookeeper.config.common.RelationMockData;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.entity.ServerRelation;
import com.hy.zookeeper.config.service.IServerRelationService;
import com.hy.zookeeper.config.util.ObjectToValueMap;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RelationControllerTest extends ControllerTestBase{

	@Resource
	private IServerRelationService relationService;
	
	
	private static final String uri = "/relation";
	
	private ServerRelation relation = null;
	
	@Resource
	private ServerEntranceRepository entranceRepository;
	
	@Before
	public void setup(){
		ServerEntrance entrance = entranceRepository.findByServerId(RelationMockData.mysqlServerId).get(0);
		relation = RelationMockData.getRelationMockData(entrance);
	}
	
	private MultiValueMap<String, String> getSearchValueMap(){
		MultiValueMap<String, String> valueMap = ObjectToValueMap.getObjectValueMap(relation);
		valueMap.add("page", "1");
		valueMap.add("rows", "5");
		return valueMap;
	}
	
	@Test
	public void index(){
		sendRequest(uri+"/index", true, getSearchValueMap());
	}
	
	@Test
	public void importTest(){
		sendRequest(uri+"/import", true, getSearchValueMap());
	}
	
	@Test
	public void tree(){
		relationService.saveRelation(relation); 
		MvcResult result = sendRequest(uri+"/tree", false, getSearchValueMap());
		assertRows(result);
	}
	
	@Test
	public void allRelation(){
		relationService.saveRelation(relation); 
		MvcResult result = sendRequest(uri+"/allRelation", false, getSearchValueMap());
		assertRows(result);
	}
	
	@Test
	public void editPage(){
		relationService.saveRelation(relation);
		sendRequest(uri+"/editPage", true, getSearchValueMap());
	}
	
	@Test
	public void save() {
		MvcResult result = sendRequest(uri+"/save", false, getSearchValueMap());
		assertSucess(result);
	}
	
	@Test 
	public void zdelete(){
		relationService.saveRelation(relation);
		MvcResult result = sendRequest(uri+"/delete", false, getSearchValueMap());
		assertSucess(result);
	}
	
	@Test
	public void zdelByServerId(){
		relationService.saveRelation(relation);
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
		valueMap.add("serverId", RelationMockData.serverIds[0]);
		MvcResult result = sendRequest(uri+"/delByServerId", false, valueMap);
		assertSucess(result);
	}
	
	@Test
	public void autoConfig() {
		relationService.deleteBySrcServerId(RelationMockData.serverIds[0]);
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
		valueMap.add("serverTypes", JSON.toJSONString(RelationMockData.serverTypes));
		valueMap.add("serverIds", JSON.toJSONString(RelationMockData.serverIds));
		
		MvcResult result = sendRequest(uri+"/autoConfig", false, valueMap);
		assertSucess(result);
		
		assertRelationNotEmty(RelationMockData.serverIds);
	}

	@Test
	public void imposedConfig() {
		relationService.deleteBySrcServerId(RelationMockData.serverIds[0]);
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
		valueMap.add("serverTypes", JSON.toJSONString(RelationMockData.serverTypes));
		valueMap.add("serverIds", JSON.toJSONString(RelationMockData.serverIds));
		
		MvcResult result = sendRequest(uri+"/imposedConfig", false, valueMap);
		assertSucess(result);
		
		assertRelationNotEmty(RelationMockData.serverIds);
	}
	
	@Test
	public void validate() {
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
		
		MvcResult result = sendRequest(uri+"/validate", false, valueMap);
		assertSucess(result);
	}
	
	@Test
	public void exportRelation(){
		sendRequest(uri+"/exportRelation", true, new LinkedMultiValueMap<>());
	}
	
	@Test
	public void upload(){
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		String filePath = path+"static/流向导入文件.xls";
		File fileReq = new File(filePath);
		MvcResult result = null;
		try (InputStream is = new FileInputStream(fileReq)){
			MockMultipartFile file = new MockMultipartFile("file", is);
			result = mockMvc.perform(MockMvcRequestBuilders.fileUpload(uri+"/upload").file(file)).andReturn();
		} catch (Exception e) {
			Assert.fail("上传文件失败");
		}
		Assert.assertTrue(result.getResponse().getStatus() == 200);
		String content = getContentAsString(result);
		Assert.assertTrue("导入成功".equals(content));
	}
	
	@Test
	public void mapData(){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("serverIds", JSON.toJSONString(RelationMockData.serverIds));
		sendRequest(uri+"/mapData", false, valueMap);
	}
	
	@Test
	public void deleteLine(){
		ServerRelation relation = RelationMockData.relation;
		relationService.saveRelation(relation);
		String lineId = RelationMockData.getRelationLineId(relation);
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("lineId", lineId);
		assertSucess(sendRequest(uri+"/deleteLine", false, valueMap));
	}
	
	@Test
	public void addLine(){
		ServerRelation relation = RelationMockData.relation;
		String lineId = RelationMockData.getRelationLineId(relation);
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("srcNode", lineId.split(RelationMockData.split2)[0]);
		valueMap.add("destNode", lineId.split(RelationMockData.split2)[1]);
		valueMap.add("serverIds", JSON.toJSONString(RelationMockData.serverIds));
		assertSucess(sendRequest(uri+"/addLine", false, valueMap));
	}
	
	@Test
	public void batchDelete(){
		ServerRelation relation = RelationMockData.relation;
		relationService.saveRelation(relation);
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.add("ids", JSON.toJSONString(RelationMockData.serverIds));
		assertSucess(sendRequest(uri+"/batchDelete", false, valueMap));
	}
	
	/**
	 * 判断流向不为空
	 * @param serverIds 服务id
	 */
	private void assertRelationNotEmty(String[] serverIds){
		Assert.assertFalse(relationService.getRelationByServerIds(Arrays.asList(serverIds)).isEmpty());
	}
}
