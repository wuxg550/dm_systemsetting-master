package com.hy.zookeeper.config.util;

import java.util.UUID;

public class UUIDTOOL {
	
	private UUIDTOOL(){}

	public static String getuuid(int length){
		return UUID.randomUUID().toString().replace("-", "").substring(0, length);
	}
}
