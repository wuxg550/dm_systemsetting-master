package com.hy.zookeeper.config.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;

import com.hy.zookeeper.config.TestBase;
import com.hy.zookeeper.config.util.StringUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CuratorClientTest extends TestBase{
    
	CuratorClient client = null;
	
	@Value("${zookeeper.address}")
	private String zkConnectString;
	
	private String leagalPath = "/STEST/001";
	private String illegalPath = "//";
	
	@Before
	public void init(){
		client = CuratorClient.getInstance(zkConnectString);
	}
	
	private void createLeagalPath(){
		client.createIfPathNotExist(leagalPath, leagalPath);
	}
	
    @Test
    public void updateNode(){
    	createLeagalPath();
    	
    	Assert.assertTrue(client.updateNodeData(leagalPath, leagalPath));
    	
    	Assert.assertFalse(client.updateNodeData(illegalPath, illegalPath));
    }
    
    @Test
    public void nodeAddListener(){
    	Assert.assertTrue(client.nodeAddListener(client, leagalPath, (client, event) -> {}));
    }
    
    @Test
    public void haveClidren(){
    	Assert.assertFalse(client.haveClidren(illegalPath));
    }
    
    @Test
    public void getNodeData(){
    	Assert.assertTrue(StringUtil.isBlank(client.getNodeData(illegalPath)));
    }
    
    @Test
    public void getForNodeData(){
    	Assert.assertTrue(StringUtil.isNotBlank(client.getForNodeData(leagalPath).getData()));
    	
    	Assert.assertTrue(StringUtil.isBlank(client.getForNodeData(illegalPath).getData()));
    }
    
    @Test
    public void getClildrenList(){
    	Assert.assertFalse(client.getClildrenList(leagalPath.substring(0, leagalPath.lastIndexOf('/'))).isEmpty());
    	
    	Assert.assertTrue(client.getClildrenList(illegalPath).isEmpty());
    }
    
    @Test
    public void deleteNode(){
    	createLeagalPath();
    	
    	Assert.assertTrue(client.deleteNode(leagalPath));
    	
    	Assert.assertFalse(client.deleteNode(illegalPath));
    	
    	createLeagalPath();
    }
    
    public static void main(String[] args) {
    	CuratorClient client = CuratorClient.getInstance("127.0.0.1:2181");
    	client.deleteNode("/S4/servers/CSGateway/instance/74ac48e6a26347cf93997f77b061578f");
	}
}
