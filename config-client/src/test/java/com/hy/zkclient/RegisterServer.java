package com.hy.zkclient;

import java.util.List;

import cn.hy.config.zkclient.platform.Config.FunctionCodesObserver;
import cn.hy.config.zkclient.platform.RegisterServerByXml;
import cn.hy.config.zkclient.platform.common.AbstractRelationObserver;
import cn.hy.config.zkclient.zknode.MyServerInfo;
import cn.hy.config.zkclient.zknode.RelationConfig;
import cn.hy.config.zkclient.zknode.RunningConfig;
import cn.hy.config.zkclient.zknode.interfaces.IFunctionCodesListener;

import com.alibaba.fastjson.JSON;

public class RegisterServer {

	public static void main(String[] args) throws InterruptedException {
		RegisterServerByXml client = new RegisterServerByXml("127.0.0.1:2182","D:/webapp/workspace/ConfigClient/src/main/java/cn/hy/config/zkclient/serverInfo.xml");
		// 构造流向订阅者
		AbstractRelationObserver o1 = new AbstractRelationObserver("test1"){

			@Override
			public void handle(RelationConfig relation) {
				System.out.println(relation.getSrcConsumerFc());
				RegisterServerByXml.removeObserver(relation.getSrcConsumerFc());
			}
			
		};
		// 构造流向订阅者
		AbstractRelationObserver o2 = new AbstractRelationObserver("test2"){

			@Override
			public void handle(RelationConfig relation) {
				System.out.println(relation.getSrcConsumerFc());
				System.out.println(JSON.toJSONString(relation));
			}
			
		};
		
		System.out.println("zk在线状态："+client.getZookeeperState("127.0.0.1", 2183));
		
		// 添加流向订阅者
		try {
			client.registerServer(o1, o2);
		} catch (Exception e1) {
		}
		
		MyServerInfo info = client.readServerInfo().toJavaObject(MyServerInfo.class);
		// 设置runningConfig监听
		client.runningConfigListener(info, (data, path)->{
			System.out.println("节点数据："+data);
			System.out.println("节点路径："+path);
			
			// 数据解析
			List<RunningConfig> configs = JSON.parseArray(data, RunningConfig.class);;
			System.out.println(configs);
			
		});
		
		Thread.sleep(5000);
		List<RelationConfig> rs = null;
		// 根据功能码获取流向
		rs = client.getRelation("FC_GET_MYSQL_CONNECTIONSTRING_0001");
		
		// 功能节点处理
		IFunctionCodesListener fcl=(fc, relations)->{
			System.out.println(fc);
			System.out.println(relations.size());
		};
		// 功能节点订阅者
		FunctionCodesObserver fcObserver = client.new FunctionCodesObserver(fcl);
		try {
			// 添加订阅
			fcObserver.addFunctionCodesObserver("FC_GET_MYSQL_CONNECTIONSTRING_0001", "get02");
		} catch (Exception e) {
			// 设置监听失败
		}
		
		Thread.sleep(5000);
		
		// 移除订阅者
//		fcObserver.removeFunctionCodesObserver("FC_GET_MYSQL_CONNECTIONSTRING_0001", "get02");
		
		Thread.sleep(200000);
		
		System.out.println("close...");
		client.close();
	}
}
