package com.hy.zookeeper.config.util;

public class ServerRelationUtil {

	public static String getRelationId(String srcServerId, String srcConsumerFc, String destServerId, String destProviderFc){
		String str = srcServerId + "_" + srcConsumerFc + "_" + destServerId + "_" + destProviderFc;
		String id = String.valueOf(str.hashCode());
		
		if(id.length() > 32){
			return id.substring(0, 32);
		}else {
			return id;
		}
		
	}
	
	private ServerRelationUtil(){}
}
