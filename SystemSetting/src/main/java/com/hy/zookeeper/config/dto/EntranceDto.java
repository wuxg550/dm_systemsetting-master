package com.hy.zookeeper.config.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.util.StringUtil;

/**
 * 入口传输类
 * @author jianweng
 *
 */
public class EntranceDto {

	private String id;
	//端口
	private Integer port;
	//入口协议类型(HTTP_POST,HTTP_GET,TCP,SDK)
	private String protocol;
	//入口地址
	private String url;
	
	// 功能码集合
	private List<String> fcs = new ArrayList<>();
	
	private String userName;
	private String password;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getFcs() {
		return fcs;
	}

	public void setFcs(List<String> fcs) {
		this.fcs = fcs;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public static Map<String, EntranceDto> getEntranceDtoFcMap(String entranceNodeData){
		// 功能码-入口DTO集合
		Map<String, EntranceDto> fcEntranceMap = new HashMap<>();
		List<EntranceDto> entranceDtoList = new ArrayList<>();
		if(StringUtils.isNotBlank(entranceNodeData)){
			entranceDtoList.addAll(JSON.parseArray(entranceNodeData, EntranceDto.class));
		}
		for(EntranceDto e : entranceDtoList){
			if(e.getFcs() != null){
				for(String fc : e.getFcs()){
					fcEntranceMap.put(fc, e);
				}
			}
		}
		
		return fcEntranceMap;
	}
	
	public static EntranceDto getDto(ServerEntrance e){
		EntranceDto entranceDto = new EntranceDto();
		
		BeanUtils.copyProperties(e, entranceDto);
		if(StringUtil.isNotBlank(e.getFcs())){
			entranceDto.setFcs(JSON.parseArray(e.getFcs(), String.class));
		}
		entranceDto.setUrl(e.getURL());
		
		return entranceDto;
	}
}
