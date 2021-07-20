package com.hy.zookeeper.config.util;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.hy.zookeeper.config.TestBase;

public class ZookeeperUtilTest extends TestBase{

	@Value("${zookeeper.address}")
	private String zkAddress;
	
	@Test
	public void test001getZKclint(){
		ZookeeperUtil.getZkclint(zkAddress);
	}
	
}
