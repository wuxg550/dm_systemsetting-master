package com.hy.zookeeper.config;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional // 配置事务回滚，单元测试不修改数据库
public class TestBase {

	@Value("${zookeeper.address}")
	private String zkConnectString;

	public String getZkConnectString() {
		return zkConnectString;
	}

}
