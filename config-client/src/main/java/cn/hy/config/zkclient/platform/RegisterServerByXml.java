package cn.hy.config.zkclient.platform;

import java.util.List;
import java.util.UUID;

import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.KeeperException.ConnectionLossException;

import cn.hy.config.zkclient.platform.common.AbstractRelationObserver;
import cn.hy.config.zkclient.platform.common.RelationCache;
import cn.hy.config.zkclient.platform.common.RelationListener;
import cn.hy.config.zkclient.zknode.MyServerInfo;
import cn.hy.config.zkclient.zknode.RelationConfig;
import cn.hy.config.zkclient.zknode.interfaces.IRelationListener;

import com.alibaba.fastjson.JSONObject;

/**
 * 使用xml注册服务
 * @author jianweng
 *
 */
public class RegisterServerByXml extends XmlConfig{

	/**
	 * 本地配置zookeeper地址
	 */
	private String zookeeperAddress = "127.0.0.1:2181";
	
	/**
	 * 
	 * @param zkAddress zookeeper地址
	 * @param xmlPath 服务信息xml文件路径
	 * @param listeners
	 */
	public RegisterServerByXml(String zkAddress, String xmlPath,
			ConnectionStateListener... listeners) {
		super(zkAddress, xmlPath, listeners);
		zookeeperAddress = zkAddress;
		logger.info("zookeeper地址初始化："+zookeeperAddress);
	}

	/**
	 * 使用默认的流向监听处理器  
	 * {@link cn.hy.config.zkclient.platform.common.RelationListener}
	 * @param observers 流向变更订阅处理类集合，不需处理可不传
	 * @throws Exception 
	 */
	public void registerServer(AbstractRelationObserver... observers) throws Exception{
		if(observers != null && observers.length > 0){
			for(AbstractRelationObserver o : observers){
				addObserver(o);
			}
		}
		
		registerServer();
	}
	
	/**
	 * 使用默认的流向监听处理器  
	 * {@link cn.hy.config.zkclient.platform.common.RelationListener}
	 * @throws Exception 
	 */
	public void registerServer() throws Exception{
		registerServer(new RelationListener());
	}
	
	/**
	 * 自定义流向处理监听器
	 * @param listener
	 * @throws Exception 
	 */
	public void registerServer(IRelationListener listener) throws Exception{
		JSONObject serverinfoJSONObj = readServerInfo();
		MyServerInfo serverInfo = serverinfoJSONObj.toJavaObject(MyServerInfo.class);
		if (serverInfo.getServerId() == null || "".equals(serverInfo.getServerId())) {
			serverInfo.setServerId(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		
		modifyXmlAll(serverInfo);
		
		if (serverInfo.getAddress() == null || "".equals(serverInfo.getAddress() )) {
			String ip = getLocalIp();
			serverInfo.setAddress(ip);
		}
		
		registerAllByXml(serverInfo,listener);
	}
	
	/**
	 * 获取流向信息，无流向则返回null
	 * @param srcConsumerFc 源服务功能码
	 * @return
	 */
	public List<RelationConfig> getRelation(String srcConsumerFc){
		return RelationCache.getRelationList(srcConsumerFc);
	}
	
	/**
	 * 新增一个流向变更处理类，相同的srcConsumerFc可存在多个处理类
	 * 注意：【AbstractRelationObserver流向处理类的调用依赖本jar中实现的流向监听器，若流向监听是自行实现的，则此类不会自动被调用】
	 * @param observer
	 */
	public static void addObserver(AbstractRelationObserver observer){
		RelationCache.addObserver(observer);
	}
	
	/**
	 * 移除流向变更处理类
	 * @param srcConsumerFc
	 */
	public static void removeObserver(String srcConsumerFc){
		RelationCache.removeObserver(srcConsumerFc);
	}
	
	/**
	 * 关闭zookeeper链接
	 */
	public void closeClient(){
		super.getCurator().close();
	}
}
