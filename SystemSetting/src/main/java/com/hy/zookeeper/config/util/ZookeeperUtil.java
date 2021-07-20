package com.hy.zookeeper.config.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.common.ResultRuok;
import com.hy.zookeeper.config.common.ResultStat;
import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.enums.CommandConstEnum;


/**      
 * zk操作工具类  
 * @version    
 */
public class ZookeeperUtil {
	
	private ZookeeperUtil(){}

	protected static final Logger logger = LoggerFactory.getLogger(ZookeeperUtil.class);
	
	private static String region = "S4";
	
	/**
	 * 节点分隔符
	 */
	public static final String SEPARATOR = "/"; 
	
	/**
	 * 节点名
	 */
	public static final String SERVERS = "/servers"; 
	public static final String RELATIONCONFIG = "/relationConfig"; 
	public static final String FUNELEMENT = "/funElement"; 
	public static final String PROVIDER = "/provider";
	public static final String CONSUMER = "/consumer";
	public static final String INSTANCE ="/instance";
	public static final String INFO = "/info";
	public static final String IP = "/address";
	public static final String ENTRANCE = "/entrance";
	public static final String REGISTEDFUNTIOANS = "/registedFunctions";
	public static final String RELATION = "/relation";
	public static final String RUNNINGCONFIG = "/runningConfig";
	public static final String ONLINE = "/online";
	
	// 集群信息根节点
	public static final String VOTEE = "/votee";
	public static final String ROLE = "/role";
	
	public static final String FUNCTION_CODES = "/functionCodes";
	
	/**
	 *  标识由系统配置服务注册的第三方服务
	 */
	public static final String ONLINEDATA = "platformConfig";
	
	// 标识由系统配置服务注册的服务
	public static final String SERVICETYPE = "/service_type";
	
	private static CuratorClient curatorClient = null;
	
	private static final int SESSION_TIMEOUT = 5000;
	private static final int CONNECTION_TIMEOUT = 5000;

	public static CuratorClient getCuratorClient() {
		return curatorClient;
	}

	public static void setCuratorClient(CuratorClient curatorClient) {
		ZookeeperUtil.curatorClient = curatorClient;
	}
	
	private static String[] getResultArray(Zkserver serverInfo, CommandConstEnum commandConstEnum){
		String[] resultArray = null;
		try {
			String cmdResult = FourLetterWordMain.send4LetterWord(serverInfo.getIp(), Integer.parseInt(serverInfo.getPort()), commandConstEnum.getVal());
			if (StringUtils.isNotBlank(cmdResult)) {
				resultArray = cmdResult.split("\n");
			}
		} catch (IOException e) {
			logger.error("获取4字命令异常", e);
		}
		return resultArray;
	}
	
	/**
	 * 四字命令stat
	 * @param serverInfo
	 * @return
	 */
	public static ResultStat stat(Zkserver serverInfo){
		String[] resultArray = getResultArray(serverInfo, CommandConstEnum.STAT);
		if (resultArray != null) {
			ResultStat resultStat = new ResultStat();
			for (String rs : resultArray){
				if (rs.indexOf("Zookeeper version:") != -1) {
					resultStat.setZookeeperVersion(rs.replace("Zookeeper version:", "").trim());
				}else if (rs.indexOf("Mode:") != -1) {
					resultStat.setMode(StringUtils.deleteWhitespace(rs.replace("Mode:", "")));
				}
			}
			return resultStat;
		}
		return null;
	}
	/**
	 * 四字命令ruok
	 * @param serverInfo
	 * @return
	 */
	public static ResultRuok ruok(Zkserver serverInfo){
		String[] resultArray = getResultArray(serverInfo, CommandConstEnum.RUOK);
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
	/**
	 * 四字命令conf
	 * @param serverInfo
	 * @return
	 * @throws IOException 
	 */
	public static String conf(Zkserver serverInfo) throws IOException{
		return FourLetterWordMain.send4LetterWord(serverInfo.getIp(), Integer.parseInt(serverInfo.getPort()), CommandConstEnum.CONF.getVal());
	}
	
	public static CuratorFramework getZkclint(String host){
		// 连接 ZooKeeper 
		CuratorFramework framework = CuratorFrameworkFactory.
                newClient(host, SESSION_TIMEOUT, CONNECTION_TIMEOUT, new ExponentialBackoffRetry(1000,10));
		framework.start();
		return framework;
	}
	
	public static String getRegion() {
		return region;
	}

	public static void setRegion(String region) {
		ZookeeperUtil.region = region;
	}

	/**
	 * 获取服务流向节点路径
	 * @param serverType
	 * @param serverId
	 * @return
	 */
	public static String getRelationPath(String serverType, String serverId){
		return ZookeeperUtil.SEPARATOR + region 
				+ ZookeeperUtil.SERVERS
				+ZookeeperUtil.SEPARATOR 
				+ serverType 
				+ ZookeeperUtil.INSTANCE
				+ZookeeperUtil.SEPARATOR 
				+ serverId 
				+ ZookeeperUtil.RELATION;
	}
	
	/**
	 * 获取functionCodes基路径 ../functionCodes
	 * @return
	 */
	public static String getFunctionCodesPath(){
		return ZookeeperUtil.SEPARATOR + region 
				+ ZookeeperUtil.FUNCTION_CODES;
	}
	
	/**
	 * 获取functionCodes子节点下服务id节点路径    ../functionCodes/FC_XXXX_001/11111
	 * @param fc
	 * @param serverId
	 * @return
	 */
	public static String getFunctionCodesFcIdPath(String fc, String serverId){
		return getFunctionCodesPath()
				+ ZookeeperUtil.SEPARATOR + fc 
				+ ZookeeperUtil.SEPARATOR + serverId;
	}
}
