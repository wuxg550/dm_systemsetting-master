平台zookeeper配置中心注册监听插件

=============版本1.0.9-RELEASES新增功能说明：======================
增加解密配置文件值功能

对于springboot项目，配置文件只需添加一下三项配置即可
#启动加载初始化器
context.initializer.classes=cn.hy.config.decrypt.PropertyDecryptContextInitializer
#解密密钥
decrypt.cipher=88886666
#需要解密的键名
decrypt.keys=spring.datasource.username,spring.datasource.password

对于spring项目使用占位符形式引用properties到xml的，需要以下配置（其中system.properties未配置文件）
<bean id="configProperties"
    class="cn.hy.config.decrypt.EncrypPropertyPlaceholderConfigurer">
    <property name="locations">
      <list>
          <value>classpath:config/system.properties</value>
      </list>  
    </property>
  </bean>

=============版本1.0.7-SNAPSHOT新增功能说明：======================
1.Config抽象基类添加根据功能码获取流向数据方法getFunctionCodesRelation(String fc)
2.添加Config内部类FunctionCodesObserver订阅FunctionCodes子节点流向

RegisterServerByXml client = new    	RegisterServerByXml("127.0.0.1:2183","D:/serverInfo.xml");
		client.registerServer();
		// 功能节点处理
		IFunctionCodesListener fcl=(fc, relation)->{
			System.out.println(fc);
			System.out.println(JSON.toJSONString(relation));
		};
		// 功能节点订阅者
		FunctionCodesObserver fcObserver = client.new FunctionCodesObserver(fcl);
		try {
			// 添加订阅
			fcObserver.addFunctionCodesObserver("FC_GET_MYSQL_CONNECTIONSTRING_0001", "get02");
		} catch (Exception e) {
			// 设置监听失败
		}
		// 移除订阅者
		fcObserver.removeFunctionCodesObserver("FC_GET_MYSQL_CONNECTIONSTRING_0001", "get02");
====================版本1.0.7-SNAPSHOT说明结束====================

=============版本1.0.6-SNAPSHOT新增功能说明：======================
1.Config抽象基类增加自动获取本机ip地址方法getLocalIp()
2.新增RegisterServerByXml类简化注册
3.新增AbstractRelationObserver抽象类对不同流向变更分别作处理，注意：【此流向处理类的调用依赖本jar中实现的流向监听器，若流向监听是自行实现的，则此类不会自动被调用】
4.增加runningConfig节点监听接口支持，节点数据结构为RunningConfig类数组
[{"cfgkey":"test","cfgval":"test1","cfgname":"测试"}]

具体例子：

// 创建客户端，参数为zookeeper地址及服务xml文件路径
RegisterServerByXml client = new RegisterServerByXml("127.0.0.1:2181","D:/serverInfo.xml");
// 处理类1 test1为源服务消费功能码
AbstractRelationObserver o1 = new AbstractRelationObserver("test1"){
			//relation为源服务消费功能码对应的流向
			@Override
			public void handle(RelationConfig relation) {
				System.out.println(relation.getSrcConsumerFc());
				// 移除处理类
				client.removeObserver(relation.getSrcConsumerFc());
			}
		};
// 处理类2
AbstractRelationObserver o2 = new AbstractRelationObserver("test2"){
			@Override
			public void handle(RelationConfig relation) {
				System.out.println(relation.getSrcConsumerFc());
			}
		};
// 注册
//client.registerServer(o1, o2);
//下面代码效果同上，addObserver方法依赖本组件实现的流向
client.addObserver(o1);
client.addObserver(o2);
client.registerServer();

// 获取流向
List<RelationConfig> relations = client.getRelation("test1");

MyServerInfo info = client.readServerInfo().toJavaObject(MyServerInfo.class);
// runningConfig节点监听
client.runningConfigListener(info, (data, path)->{
	System.out.println("节点数据："+data);
	System.out.println("节点路径："+path);
	// 数据解析
	List<RunningConfig> configs = JSON.parseArray(data, 							RunningConfig.class);
	System.out.println(configs);
});

====================版本1.0.6-SNAPSHOT说明结束====================

=========================以下为旧版本说明==========================

插件使用说明
1.服务信息实体需要实现jar中三个接口：
cn.hy.config.zkclient.zknode.interfaces.IServerInfo
cn.hy.config.zkclient.zknode.interfaces.IEntrances
cn.hy.config.zkclient.zknode.interfaces.IAddress

例：
public class MyServerInfo implements IServerInfo, IEntrances, IAddress {
	private String serverId;
	private String serverType;
	//服务名
	private String serverName;
	//机构编码
	private String orgCode;
	private String AddressType;
	private String address;
	private List<MyEntrance> entrances;
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getAddressType() {
		return AddressType;
	}
	public void setAddressType(String addressType) {
		AddressType = addressType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@SuppressWarnings("unchecked")
	public List<MyEntrance> getEntrances() {
		return entrances;
	}
	public void setEntrances(List<MyEntrance> entrances) {
		this.entrances = entrances;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
}

其中入口信息需要实现cn.hy.config.zkclient.zknode.interfaces.IEntrance接口
public class MyEntrance implements IEntrance {
	//端口
	private Integer port;
	//入口协议类型(HTTP_POST,HTTP_GET,TCP,SDK)
	private String protocol;
	//入口地址
	private String url;
	// 功能码集合
	private List<String> fcs;
	private String userName;
	private String password;
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getFcs() {
		return fcs;
	}
	public void setFcs(List<String> fcs) {
		this.fcs = fcs;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
2.客户端抽象类继承
客户端抽象类有两种：
一、cn.hy.config.zkclient.platform.Config
可自己考虑存储服务自身信息的方式。
例：
public class ClientConnect extends Config{
	private static final Logger logger = Logger.getLogger(ClientConnect.class);
	/**
	 * 继承抽象类构造方法，注册zookeeper链接
	 * @param zkAddress
	 * @param listeners
	 */
	public ClientConnect(String zkAddress, ConnectionStateListener... listeners) {
		super(zkAddress, listeners);
	}
	/**
	 * 注册所有服务信息
	 * @param test
	 */
	public void registerAll(MyServerInfo test){
		super.registerInfo(test).registerAddress(test).registerEntrance(test);
	}
	/**
	 * 监听流向数据
	 * @param test
	 */
	public void relationListener(MyServerInfo test){
		relationListener(test, new IRelationListener() {
			@Override
			public void handleDataChange(String relationJson) throws Exception {
				logger.debug("流向发生变化："+relationJson);
				List<RelationConfig> relations = JSON.parseArray(relationJson, RelationConfig.class);
				if(relations!=null&&relations.size()>0){
					RelationConfigDao relationConfigDao = SpringContextHolder.getBean(RelationConfigDao.class);
					relationConfigDao.updateAll(relations);
					relationConfigDao.reloadCache();
					logger.info("保存流向变更记录，共"+relations.size()+"条");
				}
			}
		});
	}
	/**
	 * 关闭zookeeper链接
	 */
	public void closeClient(){
		super.getCurator().close();
	}
}

二、cn.hy.config.zkclient.platform.XmlConfig
默认使用了xml配置文件存储自身服务信息，需要传入xml路径。
例：
public class XmlClientConnect extends XmlConfig{
	public XmlClientConnect(String zkAddress, String infoXmlPath, ConnectionStateListener... listeners) {
		super(zkAddress, infoXmlPath, listeners);
	}
	/**
	 * 关闭zookeeper链接
	 */
	public void closeClient(){
		super.getCurator().close();
	}
}

三、根据服务架构，在服务中加入监听器，监听服务启动和服务关闭事件。
服务启动时，实例化ClientConnect或XmlClientConnect，传入配置文件中的zookeeper的IP地址（以及其他信息）
以下例子是服务启动 使用XmlClientConnect：
/**
 * 创建zookeeper链接
 */
test = new XmlClientConnect(zookeeperAddress,xmlPath, new ConnectionStateListener() {
	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		if (newState == ConnectionState.LOST) {
			logger.error("ZooKeeper连接丢失");
		} else if (newState == ConnectionState.CONNECTED) {
			logger.info("ZooKeeper创建连接");
		} else if (newState == ConnectionState.RECONNECTED) {
			logger.info("ZooKeeper连接重连");
		} else if (newState == ConnectionState.SUSPENDED) {
			logger.error("ZooKeeper连接断开");
		}
	}
});
//读取服务信息,并设置服务实际ID和IP地址
JSONObject serverinfoJSONObj = test.readServerInfo();
MyServerInfo serverInfo = serverinfoJSONObj.toJavaObject(MyServerInfo.class);
if (StringUtils.isEmpty(serverInfo.getServerId())) {
	serverInfo.setServerId(UUID.randomUUID().toString());
}
if (StringUtils.isEmpty(serverInfo.getAddress())) {
	String ip = "127.0.0.1";
	try {
		ip = InetAddress.getLocalHost().getHostAddress();
	} catch (UnknownHostException e) {
		e.printStackTrace();
	}
	serverInfo.setAddress(ip);
}
//更新xml配置文件
test.modifyXmlAll(serverInfo);
//注册服务信息并监听流向变化
test.registerAllByXml(serverInfo, new IRelationListener() {
	@Override
	public void handleDataChange(String relationJson) throws Exception {
		logger.debug("流向发生变化："+relationJson);
		List<RelationConfig> relations = JSON.parseArray(relationJson, RelationConfig.class);
		if(relations!=null&&relations.size()>0){
			RelationConfigDao relationConfigDao = SpringContextHolder.getBean(RelationConfigDao.class);
			relationConfigDao.updateAll(relations);
			relationConfigDao.reloadCache();
			logger.info("保存流向变更记录，共"+relations.size()+"条");
		}
	}
});

完整例子：

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.hy.config.zkclient.zknode.interfaces.IRelationListener;
import cn.hy.esb.configclient.MyServerInfo;
import cn.hy.esb.configclient.XmlClientConnect;
import cn.hy.esb.dao.impl.RelationConfigDao;
import cn.hy.esb.entity.RelationConfig;
import cn.hy.esb.util.SpringContextHolder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RegisterZookeeper implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(RegisterZookeeper.class);
//	private static PropertiesLoader propertiesLoader = new PropertiesLoader("/config/system.properties");
	/**
	 * 本地配置zookeeper地址
	 */
	private String zookeeperAddress = "192.168.2.199";//SysContext.getSystemConfig("zookeeper.config.ip");
	/**
	 * 本地服务信息配置文件
	 */
	private String xmlPath = SpringContextHolder.getResourceRootRealPath()+"/config/serverInfo.xml";
	private XmlClientConnect test = null;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		test.closeClient();
	}
	/**
	 * 服务启动监听
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("服务启动，准备注册.....................................");
		/**
		 * 创建zookeeper链接
		 */
		test = new XmlClientConnect(zookeeperAddress,xmlPath, new ConnectionStateListener() {
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				if (newState == ConnectionState.LOST) {
					logger.error("ZooKeeper连接丢失");
				} else if (newState == ConnectionState.CONNECTED) {
					logger.info("ZooKeeper创建连接");
				} else if (newState == ConnectionState.RECONNECTED) {
					logger.info("ZooKeeper连接重连");
				} else if (newState == ConnectionState.SUSPENDED) {
					logger.error("ZooKeeper连接断开");
				}
			}
		});
		//读取服务信息,并设置服务实际ID和IP地址
		JSONObject serverinfoJSONObj = test.readServerInfo();
		MyServerInfo serverInfo = serverinfoJSONObj.toJavaObject(MyServerInfo.class);
		if (StringUtils.isEmpty(serverInfo.getServerId())) {
			serverInfo.setServerId(UUID.randomUUID().toString());
		}
		if (StringUtils.isEmpty(serverInfo.getAddress())) {
			String ip = "127.0.0.1";
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			serverInfo.setAddress(ip);
		}
		//更新xml配置文件
		test.modifyXmlAll(serverInfo);
		//注册服务信息并监听流向变化
		test.registerAllByXml(serverInfo, new IRelationListener() {
			@Override
			public void handleDataChange(String relationJson) throws Exception {
				logger.debug("流向发生变化："+relationJson);
				List<RelationConfig> relations = JSON.parseArray(relationJson, RelationConfig.class);
				if(relations!=null&&relations.size()>0){
					RelationConfigDao relationConfigDao = SpringContextHolder.getBean(RelationConfigDao.class);
					relationConfigDao.updateAll(relations);
					relationConfigDao.reloadCache();
					logger.info("保存流向变更记录，共"+relations.size()+"条");
				}
			}
		});
	}
}
	