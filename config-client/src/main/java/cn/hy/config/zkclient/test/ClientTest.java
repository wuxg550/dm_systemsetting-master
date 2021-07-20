package cn.hy.config.zkclient.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.state.ConnectionStateListener;

import cn.hy.config.zkclient.platform.Config;

public class ClientTest extends Config{

	
	public ClientTest(String zkAddress, ConnectionStateListener... listeners) {
		super(zkAddress, listeners);
	}

	public void registerAll(MyServerInfoTest test){
		//CuratorFramework curator = 
	//	setTransaction().
		try {
			registerInfo(test).registerAddress(test).registerEntrance(test).relationListener(test, new RelationListenerImpl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		MyServerInfoTest test = new MyServerInfoTest();
		test.setAddress("10.7.2.11");
		test.setOrgCode("123456");
		test.setServerId("123d321123121sesf123q123");
		test.setServerName("注册测试");
		test.setServerType("RegisterTest");
		test.setAddressType("IP");
		List<MyEntranceTest> entrances = new ArrayList<MyEntranceTest>();
		MyEntranceTest e = new MyEntranceTest();
		e.setPort(8080);
		e.setPassword("admin");
		e.setProtocol("TCP");
		e.setUrl("get/001");
		e.setUserName("admin");
		List<String> fcs = new ArrayList<String>();
		fcs.add("get");
		e.setFcs(fcs);
		entrances.add(e);
		test.setEntrances(entrances);
		ClientTest client = new ClientTest("127.0.0.1:2181");
		client.registerAll(test);
		
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
