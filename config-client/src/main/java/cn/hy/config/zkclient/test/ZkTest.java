package cn.hy.config.zkclient.test;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.listen.Listenable;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;

import cn.hy.config.zkclient.common.NodeNameConstant;

public class ZkTest {
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int RETRY_TIME = Integer.MAX_VALUE;
	private static final int RETRY_INTERVAL = 3000;
	private static Map<String,Closeable> nodeListenerMap = new HashMap<>();
	public static final String CHARSETNAME = "UTF-8";

	public static void main(String[] args) {
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.16.203").retryPolicy(new RetryNTimes(RETRY_TIME, RETRY_INTERVAL)).connectionTimeoutMs(CONNECT_TIMEOUT).build();
		client.start();
		Listenable<ConnectionStateListener> listenableList = client.getConnectionStateListenable();
		listenableList.addListener(new ConnectionStateListener() {
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				if (newState == ConnectionState.LOST) {
					System.out.println("zookeeper连接丢失!!!!!!!!!!");
				} else if (newState == ConnectionState.CONNECTED) {
					System.out.println("zookeeper连接创建!!!!!!!!!!");
				} else if (newState == ConnectionState.RECONNECTED) {
					System.out.println("zookeeper重连成功!!!!!!!!!!");
				} else if (newState == ConnectionState.SUSPENDED) {
					System.out.println("zookeeper连接断开!!!!!!!!!!");
				}
			}
		});
		
		
		String path = NodeNameConstant.SEPARATOR + "S4" + NodeNameConstant.SERVERS 
				+ NodeNameConstant.SEPARATOR + "DataTransformerService" 
				+ NodeNameConstant.INSTANCE 
				+ NodeNameConstant.SEPARATOR + "eb36cbeecfb346658aa76f066e6cf16f"
				+ NodeNameConstant.RUNNINGCONFIG;
		final NodeCache znodeCache = new NodeCache(client, path);
		nodeListenerMap.put(path, znodeCache);
		znodeCache.getListenable().addListener(new NodeCacheListener() {
			
			public void nodeChanged() throws Exception {
				String data = new String(znodeCache.getCurrentData().getData(), CHARSETNAME);
				System.out.println(path + ":" + data);
			}
		});
		try {
			znodeCache.start();
			if (client.checkExists().forPath(path) == null) {
				client.create().creatingParentsIfNeeded().forPath(path, "".getBytes(CHARSETNAME));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (true) {
			
		}
	}

	
}
