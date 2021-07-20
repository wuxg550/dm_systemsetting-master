package com.hy.zookeeper.config;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.hy.zookeeper.config.base.BaseDaoCustomRepositoryTest;
import com.hy.zookeeper.config.client.CuratorClientTest;
import com.hy.zookeeper.config.controller.AgreementTypeControllerTest;
import com.hy.zookeeper.config.controller.EntranceControllerTest;
import com.hy.zookeeper.config.controller.NodeDataControllerTest;
import com.hy.zookeeper.config.controller.RelationControllerTest;
import com.hy.zookeeper.config.controller.RelationTempletControllerTest;
import com.hy.zookeeper.config.controller.ServerInfoControllerTest;
import com.hy.zookeeper.config.controller.ServerTypeControllerTest;
import com.hy.zookeeper.config.controller.ZookeeperControllerTest;
import com.hy.zookeeper.config.controller.ZookeeperTreeControllerTest;
import com.hy.zookeeper.config.service.impl.FunctionCodesServiceImplTest;
import com.hy.zookeeper.config.service.impl.ServerEntranceServiceImplTest;
import com.hy.zookeeper.config.service.impl.ServerRelationServiceImplTest;

@RunWith(Suite.class)
@SuiteClasses({ AgreementTypeControllerTest.class
	, EntranceControllerTest.class
	,NodeDataControllerTest.class
	,RelationControllerTest.class
	,RelationTempletControllerTest.class
	,ServerInfoControllerTest.class
	,ServerTypeControllerTest.class,
	ZookeeperControllerTest.class,
	ZookeeperTreeControllerTest.class,
	ServerRelationServiceImplTest.class,
	ServerEntranceServiceImplTest.class,
	FunctionCodesServiceImplTest.class,
	BaseDaoCustomRepositoryTest.class,
	CuratorClientTest.class})
public class AllTests {

}
