package com.hy.zookeeper.config.enums;

import java.util.HashMap;
import java.util.Map;

public enum ConfigFlagEnum {

	/**
	 * 未知
	 */
//	OTHERS("0", "未知"),
	/**
	 * 第三方服务或需要配置中心注册的服务
	 */
	REMOTE("1", "第三方服务"),
	/**
	 * 平台开发服务，服务可自行注册
	 */
	REGISTER("2", "平台服务");
	
	private String flag;
	private String remark;
	public String getFlag() {
		return flag;
	}
	public String getRemark() {
		return remark;
	}
	private ConfigFlagEnum(String flag, String remark) {
		this.flag = flag;
		this.remark = remark;
	}
	
	public static Map<String, String> getMap(){
		Map<String, String> map =  new HashMap<>();
		for(ConfigFlagEnum e : ConfigFlagEnum.values()){
			map.put(e.getFlag(), e.getRemark());
		}
		return map;
	}
}
