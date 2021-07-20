package com.hy.zookeeper.config.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hy.zookeeper.config.common.NodeData;
import com.hy.zookeeper.config.enums.CommonConst;
import com.hy.zookeeper.config.enums.NodeModelEnum;
import com.hy.zookeeper.config.util.ZookeeperUtil;

/**
 * curator 框架工具类
 * @author hrh
 *
 */
public class CuratorClient {

	protected static final Logger logger = LoggerFactory.getLogger(CuratorClient.class);
	
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int RETRY_TIME = Integer.MAX_VALUE;
	private static final int RETRY_INTERVAL = 3000;
	
	private CuratorFramework curator;

	private static volatile CuratorClient instance;
	private static void setCuratorClient(CuratorClient client){
		instance = client;
	}
	
	private CuratorFramework newCurator(String zkServers) {
		return CuratorFrameworkFactory.builder().connectString(zkServers)
				.retryPolicy(new RetryNTimes(RETRY_TIME, RETRY_INTERVAL))
				.connectionTimeoutMs(CONNECT_TIMEOUT).build();
	}

	/**
	 * zookeeper链接监听
	 * @param zkServers zookeeper地址
	 */
	private  CuratorClient(String zkServers) {
		if (curator == null) {
			curator = newCurator(zkServers);
			curator.getConnectionStateListenable().addListener(
					(CuratorFramework cf,ConnectionState cs)-> {
							if (cs == ConnectionState.LOST) {
								logger.info("连接丢失");
							} else if (cs == ConnectionState.CONNECTED) {
								logger.info("创建连接");
							} else if (cs == ConnectionState.RECONNECTED) {
								logger.info("连接重连");
							}
						});
			
			curator.getUnhandledErrorListenable().addListener((message, e)-> {
					logger.error("unhandledError:", message);
					try{
						logger.info("关闭zookeeper客户端");
						instance.close();
						logger.info("重连zookeeper客户端");
						ZookeeperUtil.getCuratorClient();
					}catch(Exception e1){
						logger.error("重连发生异常。。", e1);
					}
				});
			curator.start();
		}
	}

	
	public static CuratorClient getInstance(String zkServers) {
		if (instance == null) {
			synchronized (CuratorClient.class) {
				if (instance == null) {
					instance = new CuratorClient(zkServers);
				}
			}
		}
		return instance;
	}

	public CuratorFramework getClient(){
		
		return curator;
	}

	/**
	 * 
	 * @param path 节点路径
	 * @param content 节点数据
	 * @param nodeType 节点类型  
	 * @return 创建成功则返回真正写到的路径，否则返回null
	 */
	public String createNode(String path, String content,CreateMode nodeType){
		StringBuilder sb = new StringBuilder(path);
		try {
			return curator.create().creatingParentsIfNeeded()
					.withMode(nodeType)
					.forPath(sb.toString(), content.getBytes(CommonConst.UTF8_CHARSET));
		} catch (Exception e) {
			logger.error("创建节点:{}", path, e); 
			return null;
		}
	}
	
	/**
	 * 检查节点是否存在
	 * @param path 节点路径
	 * @return
	 */
    public boolean checkExist(String path){  
        try {  
            Stat stat = curator.checkExists().forPath(path);  
            return (stat != null);
        } catch (Exception e) {  
        	logger.error("检查节点是否存在:{}" ,path , e);  
        }
        return false;
    }
    
    /**
     * 删除节点
     * @param path 节点路径
     */
    public boolean deleteNode(String path){  
        try {
        	if(curator.checkExists().forPath(path) != null){
        		//删除已存在的节点  
                curator.delete()  
                        .guaranteed()  //删除失败，则客户端持续删除，直到节点删除为止  
                        .deletingChildrenIfNeeded()  //删除相关子节点  
                        .withVersion(-1) //无视版本，直接删除  
                        .forPath(path);
        	}
        	
        	return true;
        } catch (Exception e) {  
        	logger.error("删除节点:{}", path, e);
        	return false;
        }  
    }  
    
    /**
     * 更新节点数据
     * @param path 节点路径
     * @param data 节点数据
     */
    public boolean updateNodeData(String path,String data){  
        try {  
            //更新节点  
            curator.setData()  
                   // .withVersion(stat.getVersion())  //版本校验，与当前版本不一致则更新失败,默认值-1无视版本信息进行更新  
                  //  .inBackground(paramBackgroundCallback)  //异步修改数据，并进行回调通知  
                    .forPath(path, data.getBytes(CommonConst.UTF8_CHARSET));
            return true;
        } catch (Exception e) {  
        	logger.error("更新节点:{}", path, e);
        	return false;
        }  
    } 
    
    /**
     * 获取节点数据
     * @param path 路径
     * @return 数据的字符串
     */
    public String getNodeData(String path){
    	String dataString = "";
    	try {
			byte[] data = curator.getData().forPath(path);
			if(data != null){
				dataString = new String(data, CommonConst.UTF8_CHARSET);
			}
		} catch (Exception e) {
			logger.error("获取节点数据:{}", path, e);
		}
    	return dataString;
    }
    
    /**
     * 获取节点数据返回是nodeData
     * @param path
     * @return
     */
    public NodeData getForNodeData(String path){
    	Stat stat = new Stat();
    	NodeData nodeData = new NodeData();
    	try {
			curator.getData().storingStatIn(stat).forPath(path);
			String data = this.getNodeData(path);
 			
			
			nodeData.setData(data);
			if (stat.getEphemeralOwner() != 0) {
				nodeData.setNodeModel(NodeModelEnum.EPHEMERAL);
			}else{
				nodeData.setNodeModel(NodeModelEnum.PERSISTENT);
			}
			return nodeData;
		} catch (Exception e) {
			logger.error("获取节点NodeData:{}", path, e);
			return nodeData;
		}
    	
    }
	
	/**
	 * 获取子节点
	 * @param path 父节点
	 * @return
	 */
	public List<String> getClildrenList(String path){
		List<String> childrenList = new ArrayList<>();
		try {
			childrenList = curator.getChildren().forPath(path);
		} catch (Exception e) {
			logger.error("获取子节点:{}", path, e);
		}
		return childrenList;
	}
	
	/**
	 * 判断path下面是否有子节点
	 * @param path
	 * @return
	 */
	public boolean haveClidren(String path){
		try {
			List<String> children = curator.getChildren().forPath(path);
			return (children != null && !children.isEmpty());
		} catch (Exception e) {
			logger.error("判断是否有子节点:{}", path, e);
			return false;
		}
	}
	
    /**
     * 添加节点监听
     * @param curator CuratorFramework
     * @param path 要监听的节点
     * @param treeCacheListener 监听器
     */
    public boolean nodeAddListener(CuratorClient curator,String path,TreeCacheListener treeCacheListener){
    	
		@SuppressWarnings("resource")
		TreeCache treeCache = new TreeCache(curator.getClient(), path);  
		treeCache.getListenable().addListener(treeCacheListener);
		try {
			treeCache.start();
			return true;
		} catch (Exception e) {
			logger.error("节点树监听启动异常："+e.getMessage(), e);
			return false;
		}
	}
    
    /**
     * 创建或更新永久节点
     * @param path
     * @param data
     */
    public void createOrUpdatePath(String path,String data){
    	if(checkExist(path)){
    		updateNodeData(path, data);
    	}else{
    		createNode(path, data, CreateMode.PERSISTENT);
    	}
    }
    
    /**
     * 创建不存在的永久节点
     * @param path
     * @param data
     */
    public void createIfPathNotExist(String path, String data){
    	if(!checkExist(path)){
    		createNode(path, data, CreateMode.PERSISTENT);
    	}
    }
    
    public void close(){
    	curator.close();
    	setCuratorClient(null);
    	logger.info("zookeeper连接关闭....");
    }
}
