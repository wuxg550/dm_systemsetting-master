package cn.hy.config.zkclient.platform;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.listen.Listenable;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hy.config.zkclient.common.NodeNameConstant;
import cn.hy.config.zkclient.common.ResultRuok;
import cn.hy.config.zkclient.enums.CommandConstEnum;
import cn.hy.config.zkclient.zknode.AddressNode;
import cn.hy.config.zkclient.zknode.EntranceNode;
import cn.hy.config.zkclient.zknode.RelationConfig;
import cn.hy.config.zkclient.zknode.ServerInfoNode;
import cn.hy.config.zkclient.zknode.interfaces.IAddress;
import cn.hy.config.zkclient.zknode.interfaces.IEntrance;
import cn.hy.config.zkclient.zknode.interfaces.IEntrances;
import cn.hy.config.zkclient.zknode.interfaces.IFunctionCodesListener;
import cn.hy.config.zkclient.zknode.interfaces.INodeJsonListener;
import cn.hy.config.zkclient.zknode.interfaces.IRelationListener;
import cn.hy.config.zkclient.zknode.interfaces.IRunningConfigListener;
import cn.hy.config.zkclient.zknode.interfaces.IServerInfo;
import cn.hy.config.zkclient.zknode.interfaces.IServerKeyPoint;
import cn.hy.config.zkclient.zknode.interfaces.IZTreeListener;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;

/**
 * zookeeper注册基类
 * @author jianweng
 *
 */
public abstract class Config {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private final int CONNECT_TIMEOUT = 5000;
	private final int RETRY_TIME = Integer.MAX_VALUE;
	private final int RETRY_INTERVAL = 3000;
	private CuratorFramework curator;
	private Map<String,Closeable> nodeListenerMap = new HashMap<>();

	private String region = "S4";

	private IServerInfo infoCach;
	
	public static final String CHARSETNAME = "UTF-8";
	
	/**
	 * 设置域
	 * @param region
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * 获取curator
	 * 
	 * @return
	 */
	public CuratorFramework getCurator(){
		startClient();
		return curator;
	}

	/**
	 * 构造方法 设置zk地址,支持设置链接监听。
	 * 
	 * @param zkAddress
	 * @param listeners
	 */
	public Config(String zkAddress, ConnectionStateListener... listeners) {
		if (curator == null) {
			curator = newCurator(zkAddress);
			Listenable<ConnectionStateListener> listenableList = curator.getConnectionStateListenable();
			listenableList.addListener(new DefaultConnectionStateListener());
			if (listeners != null && listeners.length > 0) {
				for (ConnectionStateListener listener : listeners) {
					listenableList.addListener(listener);
				}
			}
		}
	}

	/**
	 * 调用CuratorFramework.start() 启动zk客户端
	 */
	private void startClient(){
		if(curator.getState() != CuratorFrameworkState.STARTED){
			curator.start();
		}
	}
	
	/**
	 * 默认添加的连接状态监听器，用于重连时注册在线节点
	 */
	class DefaultConnectionStateListener implements ConnectionStateListener{

		public void stateChanged(CuratorFramework client, ConnectionState newState) {
			if (newState == ConnectionState.LOST) {
				// zookeeper连接丢失
				logger.error("zookeeper连接丢失!!!!!!!!!!");
			} else if (newState == ConnectionState.CONNECTED) {
				// zookeeper连接创建
				logger.info("zookeeper连接创建!!!!!!!!!!");
			} else if (newState == ConnectionState.RECONNECTED) {
				// zookeeper重连   注册在线节点
				logger.info("zookeeper重连成功!!!!!!!!!!");
				setOnline(infoCach);
			} else if (newState == ConnectionState.SUSPENDED) {
				// zookeeper连接断开
				logger.error("zookeeper连接断开!!!!!!!!!!");
			}
		}
		
	}
	
	/**
	 * 启用事务
	 */
	public Config setTransaction() {
		getCurator().inTransaction();
		return this;
	}

	/**
	 * 服务信息注册
	 * @param info
	 * @return
	 * @throws Exception 
	 */
	public Config registerInfo(IServerInfo info) throws Exception{
		infoCach = info;
		ServerInfoNode infoNode = new ServerInfoNode(info);
		String content = JSON.toJSONString(infoNode);
		String path = "";
		// 创建服务id节点
		path = NodeNameConstant.SEPARATOR + region + NodeNameConstant.SERVERS 
				+ NodeNameConstant.SEPARATOR + info.getServerType() 
				+ NodeNameConstant.INSTANCE 
				+ NodeNameConstant.SEPARATOR + info.getServerId()
				+ NodeNameConstant.INFO;
		createOrSetNode(path, content);
		this.setOnline(info);
		return this;
	}

	/**
	 * 服务IP地址注册
	 * @param address
	 * @return
	 * @throws Exception 
	 */
	public Config registerAddress(IAddress address) throws Exception {
		AddressNode addNode = new AddressNode(address);
		String content = JSON.toJSONString(addNode);
		String path = "";
		// 创建服务id节点
		path = NodeNameConstant.SEPARATOR + region + NodeNameConstant.SERVERS 
				+ NodeNameConstant.SEPARATOR + address.getServerType() 
				+ NodeNameConstant.INSTANCE 
				+ NodeNameConstant.SEPARATOR + address.getServerId()
				+ NodeNameConstant.IP;
		createOrSetNode(path, content);
		return this;
	}

	/**
	 * 服务入口信息注册
	 * @param entrances
	 * @return
	 * @throws Exception 
	 */
	public Config registerEntrance(IEntrances entrances) throws Exception {
		List<EntranceNode> datalist = new ArrayList<>();
		for (IEntrance entrance : entrances.getEntrances()) {
			EntranceNode enNode = new EntranceNode(entrance);
			datalist.add(enNode);
		}
		String content = JSON.toJSONString(datalist);
		String path = "";
		// 创建服务id节点
		path = NodeNameConstant.SEPARATOR + region + NodeNameConstant.SERVERS 
				+ NodeNameConstant.SEPARATOR + entrances.getServerType() 
				+ NodeNameConstant.INSTANCE 
				+ NodeNameConstant.SEPARATOR + entrances.getServerId()
				+ NodeNameConstant.ENTRANCE;
		createOrSetNode(path, content);
		return this;
	}

	/**
	 * 设置服务离线
	 * @param server
	 */
	protected void setOffline(IServerKeyPoint server){
		try {
			String path = getOnlinePath(server);
			if(getCurator().checkExists().forPath(path) != null){
				getCurator().delete().forPath(path);
			}
		} catch (Exception e) {
			logger.error("setOffline", e);
		}
	}
	
	/**
	 * 设置服务在线
	 * @param server
	 */
	protected void setOnline(IServerKeyPoint server) {
		// 创建服务id节点
		String path = getOnlinePath(server);
		try {
			if(getCurator().checkExists().forPath(path) != null){
				getCurator().delete().forPath(path);
			}
			getCurator().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
		} catch (Exception e) {
			logger.error("setOnline", e);
		}
	}

	protected String getOnlinePath(IServerKeyPoint server){
		return NodeNameConstant.SEPARATOR + region + NodeNameConstant.SERVERS 
				+ NodeNameConstant.SEPARATOR + server.getServerType() 
				+ NodeNameConstant.INSTANCE 
				+ NodeNameConstant.SEPARATOR + server.getServerId()
				+ NodeNameConstant.ONLINE;
	}

	protected String getRelationPath(IServerKeyPoint server){
		return NodeNameConstant.SEPARATOR + region + NodeNameConstant.SERVERS 
				+ NodeNameConstant.SEPARATOR + server.getServerType() 
				+ NodeNameConstant.INSTANCE 
				+ NodeNameConstant.SEPARATOR + server.getServerId()
				+ NodeNameConstant.RELATION;
	}
	
	/**
	 * 流向节点监听
	 * @param server
	 * @param listener
	 * @return
	 */
	public Config relationListener(IServerKeyPoint server,final IRelationListener listener) {
		String path = getRelationPath(server);
		final NodeCache znodeCache = new NodeCache(getCurator(), path);
		nodeListenerMap.put(path, znodeCache);
		znodeCache.getListenable().addListener(new NodeCacheListener() {
			
			public void nodeChanged() throws Exception {
				String data = new String(znodeCache.getCurrentData().getData(), CHARSETNAME);
				listener.handleDataChange(data);
			}
		});
		try {
			znodeCache.start();
			if (getCurator().checkExists().forPath(path) == null) {
				getCurator().create().creatingParentsIfNeeded().forPath(path, "".getBytes(CHARSETNAME));
			}
		} catch (Exception e) {
			logger.error("relationListener", e);
		}
		return this;
	}

	/**
	 * 监听运行配置节点
	 * @param server
	 * @param listener
	 * @return
	 */
	public Config runningConfigListener(IServerKeyPoint server, final IRunningConfigListener listener){
		String path = NodeNameConstant.SEPARATOR + region + NodeNameConstant.SERVERS 
				+ NodeNameConstant.SEPARATOR + server.getServerType() 
				+ NodeNameConstant.INSTANCE 
				+ NodeNameConstant.SEPARATOR + server.getServerId()
				+ NodeNameConstant.RUNNINGCONFIG;
		final NodeCache znodeCache = new NodeCache(getCurator(), path);
		nodeListenerMap.put(path, znodeCache);
		znodeCache.getListenable().addListener(new NodeCacheListener() {
			
			public void nodeChanged() throws Exception {
				String data = new String(znodeCache.getCurrentData().getData(), CHARSETNAME);
				listener.handleDataChange(data, path);
			}
		});
		try {
			znodeCache.start();
			if (getCurator().checkExists().forPath(path) == null) {
				getCurator().create().creatingParentsIfNeeded().forPath(path, "".getBytes(CHARSETNAME));
			}
		} catch (Exception e) {
			logger.error("runningConfigListener", e);
		}
		return this;
	}
	
	// 其他节点监听

	/**
	 * 创建或修改节点数据
	 * @param path 路径
	 * @param content 节点数据
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public void createOrSetNode(String path, String content) throws Exception{
		if(getCurator().checkExists().forPath(path) == null){
			getCurator().create().creatingParentsIfNeeded()
			.withMode(CreateMode.PERSISTENT)
			.forPath(path, content.getBytes(CHARSETNAME));
		}else{
			getCurator().setData().forPath(path, content.getBytes(CHARSETNAME));
		}
	}
	
	/**
	 * 节点数据监听
	 * @param nodePath
	 * @param listener
	 * @return
	 * @throws Exception 
	 */
	public String nodeListener(String nodePath,final INodeJsonListener listener) throws Exception{
		String path = NodeNameConstant.SEPARATOR + region + nodePath;
		if (getCurator().checkExists().forPath(path) == null) {
			getCurator().create().creatingParentsIfNeeded().forPath(path, "".getBytes(CHARSETNAME));
		}
		final NodeCache znodeCache = new NodeCache(getCurator(), path);
		nodeListenerMap.put(path, znodeCache);
		znodeCache.getListenable().addListener(new NodeCacheListener() {
			public void nodeChanged() throws Exception {
				String data = new String(znodeCache.getCurrentData().getData(), CHARSETNAME);
				listener.handleDataChange(data);
			}
		});
		znodeCache.start();
		//FIXME 这里返回的数据和监听的数据是否存在先后问题
		String data = "";
		if(znodeCache.getCurrentData()!=null){
			data = new String(znodeCache.getCurrentData().getData(), CHARSETNAME);
		}
		return data;
	}
	
	public Config ZTreeListener(final String zpath,final IZTreeListener listener) throws Throwable{
		TreeCache treeCache = new TreeCache(getCurator(), zpath);  
		nodeListenerMap.put(zpath, treeCache);
		treeCache.getListenable().addListener(new TreeCacheListener() {
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				ChildData cdata = event.getData();
				if(cdata!=null){
					String cdata_path = cdata.getPath();
					String nodeName = cdata_path.replace(zpath, "");
					String data = null;
					if(event.getData().getData() != null){
						data = new String(event.getData().getData(), CHARSETNAME);
					}
					switch (event.getType()) {  
			           case NODE_ADDED://节点添加事件触发
			        	   listener.nodeAdded(nodeName, data);
			               break;  
			           case NODE_UPDATED://节点更新事件触发
			        	   listener.nodeUpdated(nodeName, data);
			               break;  
			           case NODE_REMOVED: //节点删除事件触发 
			        	   listener.nodeRemoved(nodeName);
			               break;  
			           default:  
			               break;
					}
				}
			}
		});
		treeCache.start();
		return this;
	}
	
	/**
	 * 关闭客户端
	 */
	public void close(){
		if(nodeListenerMap!=null&&nodeListenerMap.size()>0){
			try {
				for(Iterator<Entry<String, Closeable>> it = nodeListenerMap.entrySet().iterator(); it.hasNext();){
					Map.Entry<String, Closeable> item = it.next();
					item.getValue().close();
					it.remove();
				}
			} catch (IOException e) {
				logger.error("关闭节点监听事件异常", e);
			}
		}
		setOffline(infoCach);
		if(curator != null){
			curator.close();
		}
	}
	/**
	 * 创建zookeeper管理者
	 * 
	 * @param zkServers
	 * @return
	 */
	private CuratorFramework newCurator(String zkServers) {
		return CuratorFrameworkFactory.builder().connectString(zkServers).retryPolicy(new RetryNTimes(RETRY_TIME, RETRY_INTERVAL)).connectionTimeoutMs(CONNECT_TIMEOUT).build();
	}
	
	private String localAddress = "127.0.0.1";
	
	/**
     * 获取IP地址
     * @return IP地址
	 * @throws SocketException 
     */
	public String getLocalIp() {
        return NetUtils.getLocalAddress().getHostAddress();
    }
	
	/**
	 * 检测节点是否存在
	 * @param path
	 * @return 存在：true，异常或不存在：false
	 */
	public boolean checkPathExists(String path){
		try {
			return getCurator().checkExists().forPath(path) != null;
		} catch (Exception e) {
			logger.error("判断节点是否存在异常,path:"+path, e);
		}
		return false;
	}
	
	/**
	 * 获取节点数据
	 * @param path
	 * @return 异常或数据为空返回null
	 */
	public String getNodeData(String path){
		String dataString = null;
    	try {
			byte[] data = getCurator().getData().forPath(path);
			if(data != null){
				dataString = new String(data, CHARSETNAME);
			}
		} catch (Exception e) {
			logger.error("获取节点数据:"+path, e);
		}
    	return dataString;
	}
	
	/**
	 * 设置永久节点数据，若节点不存在则创建节点
	 * @param path
	 * @param data
	 * @return 创建成功返回true
	 */
	public boolean setNodeData(String path, String data){
		return setNodeData(path, data, CreateMode.PERSISTENT);
	}
	
	/**
	 * 设置节点数据，若节点不存在则创建节点
	 * @param path
	 * @param data
	 * @param nodeType 节点类型
	 * @return 创建成功返回true
	 */
	public boolean setNodeData(String path, String data, CreateMode nodeType){
		try{
			if(!checkPathExists(path)){
				getCurator().create().creatingParentsIfNeeded()
				   .withMode(nodeType)
				   .forPath(path, data.getBytes(CHARSETNAME));
			}else{
				getCurator().setData().forPath(path, data.getBytes(CHARSETNAME));
			}
			return true;
		}catch(Exception e){
			logger.error("设置节点数据:"+path, e);
		}
		
		return false;
	}
	
	/**
	 * 获取子节点
	 * @param path 父节点
	 * @return 返回0个或多个子节点名称
	 */
	public List<String> getClildrenList(String path){
		List<String> childrenList = new ArrayList<>();
		try {
			childrenList = getCurator().getChildren().forPath(path);
		} catch (Exception e) {
			logger.error("获取子节点:"+path, e);
		}
		return childrenList;
	}
	
	/**
	 * 获取/functionCodes下功能码子节点 如：/S4/functionCodes/FC_XXXX_001
	 * @param fc
	 * @return
	 */
	public String getFunctionCodesFcPath(String fc){
		return NodeNameConstant.getFunctionCodesFcPath(region, fc);
	}
	
	/**
	 * 根据功能码获取functionCodes节点流向数据
	 * @param fc
	 * @return 返回0条或多条流向
	 */
	public List<RelationConfig> getFunctionCodesRelation(String fc){
		List<RelationConfig> relations = new ArrayList<>();
		
		String fcPath = getFunctionCodesFcPath(fc);
		if(checkPathExists(fcPath)){
			List<String> serverIds = getClildrenList(fcPath);
			for(String id : serverIds){
				String fcIdPath = fcPath + NodeNameConstant.SEPARATOR + id;
				String data = getNodeData(fcIdPath);
				if(data != null){
					RelationConfig r = JSON.parseObject(data, RelationConfig.class);
					if(infoCach != null){
						r.setSrcServerId(infoCach.getServerId());
						r.setSrcServerType(infoCach.getServerType());
						r.setSrcConsumerFc(fc);
					}
					relations.add(r);
				}
			}
		}
		
		return relations;
	}
	
	public class FunctionCodesObserver {

		private final Logger logger = LoggerFactory.getLogger(FunctionCodesObserver.class);
		
		private final IFunctionCodesListener listener;
		
		/**
		 * FunctionCodes子节点树监听集合
		 */
		private Map<String, Closeable> fcNodeListenerMap = new ConcurrentHashMap<>();
		
		public FunctionCodesObserver(IFunctionCodesListener listener) {
			super();
			this.listener = listener;
		}

		/**
		 * 添加FunctionCodes子节点树订阅
		 * @param listener 节点变化处理器
		 * @param fc 功能码
		 * @return 返回的流向数据只有目标服务的信息
		 * @throws Exception
		 */
		public List<RelationConfig> addFunctionCodesObserver(final String fc) throws Exception{
			removeFunctionCodesObserver(fc);
			
			final String fcPath = getFunctionCodesFcPath(fc);
			TreeCache treeCache = new TreeCache(getCurator(), fcPath);
			fcNodeListenerMap.put(fc, treeCache);
			
			treeCache.getListenable().addListener(new TreeCacheListener() {
				public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
					List<RelationConfig> relations = getFunctionCodesRelation(fc);
					listener.handleDataChange(fc, relations);
				}
			});
			treeCache.start();
			
			return getFunctionCodesRelation(fc);
		}
		
		/**
		 * 批量添加FunctionCodes子节点树订阅
		 * @param listener
		 * @param fcs
		 * @return 返回的流向数据只有目标服务的信息
		 * @throws Exception
		 */
		public List<RelationConfig> addFunctionCodesObserver(final String... fcs) throws Exception{
			List<RelationConfig> relations = new ArrayList<>();
			if(fcs != null && fcs.length > 0){
				for(String fc : fcs){
					relations.addAll(addFunctionCodesObserver(fc));
				}
			}
			return relations;
		}
		
		/**
		 * 移除FunctionCodes订阅
		 * @param fc
		 */
		public void removeFunctionCodesObserver(String fc){
			if(fcNodeListenerMap.get(fc) != null){
				Closeable closeable = fcNodeListenerMap.remove(fc);
				try {
					closeable.close();
				} catch (IOException e) {
					logger.error("关闭服务功能监听失败，fc："+fc, e);
				}
			}
		}
		
		/**
		 * 批量移除FunctionCodes订阅
		 * @param fcs
		 */
		public void removeFunctionCodesObserver(String... fcs){
			if(fcs != null && fcs.length > 0){
				for(String fc : fcs){
					removeFunctionCodesObserver(fc);
				}
			}
		}
	}
	
	/**
	 * 获取zookeeper服务在线状态，true：在线，false：离线
	 * @param ip
	 * @param port
	 * @return
	 */
	public boolean getZookeeperState(String ip, Integer port){
		ResultRuok ruok  = ruok(ip, port);
		if (ruok == null) {
			return false;
		}
		return !StringUtils.isBlank(ruok.getImok());
	}
	
	/**
	 * 四字命令ruok
	 * @param serverInfo
	 * @return
	 */
	private ResultRuok ruok(String ip, Integer port){
		String[] resultArray = getResultArray(ip, port, CommandConstEnum.RUOK);
		if (resultArray != null) {
			ResultRuok resultRuok = new ResultRuok();
			for (String rs : resultArray){
				if (rs.indexOf("imok") != -1) {
					resultRuok.setImok(rs.trim());
				}
			}
			return resultRuok;
		}
		return null;
	}
	
	private String[] getResultArray(String ip, Integer port, CommandConstEnum commandConstEnum){
		String[] resultArray = null;
		try {
			String cmdResult = FourLetterWordMain.send4LetterWord(ip, port, commandConstEnum.getVal());
			if (!StringUtils.isBlank(cmdResult)) {
				resultArray = cmdResult.split("\n");
			}
		} catch (IOException e) {
			logger.error("获取zookeeper4字命令异常，请检查zookeeper服务是否已启动或网络状态是否正常");
		}
		return resultArray;
	}
}
