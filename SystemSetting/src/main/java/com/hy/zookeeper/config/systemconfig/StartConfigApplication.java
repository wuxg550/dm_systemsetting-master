package com.hy.zookeeper.config.systemconfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.dao.RelationsRepsotory;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.dao.ServerTypeRepsotory;
import com.hy.zookeeper.config.dto.AddressDto;
import com.hy.zookeeper.config.dto.CommonServerInfo;
import com.hy.zookeeper.config.dto.ServerInfoDto;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.entity.PlatformServerType;
import com.hy.zookeeper.config.enums.ConfigFlagEnum;
import com.hy.zookeeper.config.enums.OnlineStatusEnum;
import com.hy.zookeeper.config.service.IServerInfoService;
import com.hy.zookeeper.config.service.IServerRelationService;
import com.hy.zookeeper.config.util.AddressUtil;
import com.hy.zookeeper.config.util.StringUtil;
import com.hy.zookeeper.config.util.ZookeeperUtil;

/**
 * 服务启动类
 * 
 * @author hrh
 *
 */
@Component
public class StartConfigApplication implements ApplicationRunner {

	protected static final Logger logger = LoggerFactory
			.getLogger(StartConfigApplication.class);

	private static final String REMOVE_NODE_OP = "removed";

	@Autowired
	private ServerTypeRepsotory serverTypeRepsotory;

	@Autowired
	private ServerInfoRepsotory serverInfoRepsotory;

	@Autowired
	private IServerRelationService serverRelationServerice;

	@Resource
	private RelationsRepsotory relationsRepsotory;

	@Resource
	private ServerEntranceRepository entranceRepository;

	@Autowired
	private IServerInfoService serverInfoService;

	@Value("${root.region}")
	private String region;

	@Value("${zookeeper.address}")
	private String zkConnectString;

	@Value("${common.info.path}")
	private String commonInfoPath;

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		ZookeeperUtil.setRegion(region);

		intiNode(zkConnectString.trim(), serverTypeRepsotory.findAll(), region);
	}

	/**
	 * 创建默认节点
	 * 
	 * @param typeList
	 */
	public void intiNode(String onlineServerString, List<PlatformServerType> typeList,
			String region) {
		// 处理队列
		CuratorClient curator = CuratorClient.getInstance(onlineServerString);
		// 将CuratorClient 赋值给工具类 系统只有一个CuratorClient
		ZookeeperUtil.setCuratorClient(curator);

		// 初始公有信息节点
		initCommonInfoNode();

		// 清理数据库服务信息
		serverInfoService.cleanAndRegistServerInDb(null, curator);

		String functoinCodesPath = ZookeeperUtil.getFunctionCodesPath();
		curator.createOrUpdatePath(functoinCodesPath, "");

		String path = ZookeeperUtil.SEPARATOR + region + ZookeeperUtil.SERVERS;
		String relation = ZookeeperUtil.SEPARATOR + region
				+ ZookeeperUtil.RELATIONCONFIG;
		String funelement = ZookeeperUtil.SEPARATOR + region
				+ ZookeeperUtil.FUNELEMENT;
		try {
			// 创建 servers relationconfig funelement 节点
			curator.createIfPathNotExist("/" + region, "");
			curator.createIfPathNotExist(path, "");
			curator.createIfPathNotExist(relation, "");
			curator.createIfPathNotExist(funelement, "");

			String serviceTypePath = ZookeeperUtil.SEPARATOR + region
					+ ZookeeperUtil.SERVICETYPE;
			curator.createOrUpdatePath(serviceTypePath,
					JSON.toJSONString(typeList));

			// 创建服务类型节点
			for (PlatformServerType type : typeList) {

				curator.createIfPathNotExist(path + ZookeeperUtil.SEPARATOR
						+ type.getServerType(), "");

				curator.createIfPathNotExist(relation + ZookeeperUtil.SEPARATOR
						+ type.getServerType(), "");

				curator.createIfPathNotExist(path + ZookeeperUtil.SEPARATOR
						+ type.getServerType() + ZookeeperUtil.CONSUMER, "");

				curator.createIfPathNotExist(path + ZookeeperUtil.SEPARATOR
						+ type.getServerType() + ZookeeperUtil.PROVIDER, "");

				curator.createIfPathNotExist(path + ZookeeperUtil.SEPARATOR
						+ type.getServerType() + ZookeeperUtil.INSTANCE, "");

				// 建立集群信息根节点
				curator.createIfPathNotExist(path + ZookeeperUtil.SEPARATOR
						+ type.getServerType() + ZookeeperUtil.VOTEE, "集群选举节点");
			}
			// 订阅监听servers以及servers以下的节点
			curator.nodeAddListener(curator, path, new TreeCacheListener() {

				@Override
				public void childEvent(CuratorFramework client,
						TreeCacheEvent event) throws Exception {
					String data = null;
					if (event.getData() != null
							&& event.getData().getData() != null) {
						data = new String(event.getData().getData(), "UTF-8");
					}
					switch (event.getType()) {
					case NODE_ADDED:// 节点添加事件触发
						logger.info("NODE_ADDED：路径："
								+ event.getData().getPath());
						String path = event.getData().getPath();
						updateOnlineStatus("add", path, data);
						break;
					case NODE_UPDATED:// 节点更新事件触发
						logger.info("NODE_UPDATED：路径："
								+ event.getData().getPath());
						String updatedPath = event.getData().getPath();
						updateOnlineStatus("update", updatedPath, data);
						break;
					case NODE_REMOVED: // 节点删除事件触发
						logger.info("NODE_REMOVED：路径："
								+ event.getData().getPath());
						String removePath = event.getData().getPath();
						updateOnlineStatus(REMOVE_NODE_OP, removePath, "");
						break;
					default:
						break;
					}
				}
			});

			createAndSetRegionNode();
		} catch (Exception e) {
			logger.error("初始节点异常", e);
		}
	}

	/**
	 * 更新server 在线状态
	 * 
	 * @param type
	 *            节点改变类型
	 * @param path
	 *            改变的节点
	 */
	private void updateOnlineStatus(String type, String path, String data) {

		String[] pathArray = path.split("/");
		String id = "";
		String serverType = "";
		// 当路径数组的长度大于5，如：/S4/servers/type/instance/id/info/ 然后从数组当中获取该服务的id和类型
		if (pathArray.length > 5) {
			for (int i = 0; i < pathArray.length; i++) {
				if (ZookeeperUtil.INSTANCE.equals("/" + pathArray[i])) {
					id = pathArray[i + 1];
					serverType = pathArray[i - 1];
				}

			}
		}

		if ("add".equals(type)) {// 新服务注册上了保存数据库操作
			// 服务注册
			serverAdd(id, serverType, path, data, pathArray);
		} else if (REMOVE_NODE_OP.equals(type)) { // 节点删除处理逻辑
			if (ZookeeperUtil.ONLINE.equals(path.substring(
					path.lastIndexOf('/'), path.length()))) {
				String entrancePath = path.substring(0, path.lastIndexOf('/'))
						+ ZookeeperUtil.ENTRANCE;
				// 若入口节点不存在则是删除服务操作，不需要再做服务离线变更流向操作
				if (ZookeeperUtil.getCuratorClient().checkExist(entrancePath)) {
					// 服务离线
					serverInfoService.serverOffline(id,
							OnlineStatusEnum.OFFLINE.getStatus());
					serverRelationServerice.serverOnOffLine(id,
							OnlineStatusEnum.OFFLINE.getStatus());
				}
			}
		} else {
			// 服务信息更新
			serverUpdate(id, serverType, path, data);
		}

	}

	/**
	 * 若服务的公有配置信息为最新则返回true，否则返回false
	 * 
	 * @param serverInfoDto
	 * @param path
	 * @return
	 */
	public boolean updateCommonServerInfo(ServerInfoDto serverInfoDto,
			String path) {
		if (serverInfoDto == null) {
			serverInfoDto = new ServerInfoDto();
		}
		CommonServerInfo commoninfo = serverInfoService.getCommonServerInfo();
		// 判断是否要重新设置服务公有信息
		if (!commoninfo.getCascadeDomain().equals(
				serverInfoDto.getCascadeDomain())
				|| !commoninfo.getOrgId().equals(serverInfoDto.getOrgId())
				|| !commoninfo.getOrgName().equals(serverInfoDto.getOrgName())) {
			serverInfoDto.setCascadeDomain(commoninfo.getCascadeDomain());
			serverInfoDto.setOrgId(commoninfo.getOrgId());
			serverInfoDto.setOrgName(commoninfo.getOrgName());
			
			String serverInfoJson = JSON.toJSONString(serverInfoDto);
			logger.info("服务共有信息不一致，更新zookeeper节点信息，路径：{}，数据：{}", path, serverInfoJson);
			ZookeeperUtil.getCuratorClient().updateNodeData(path,
					serverInfoJson);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断数据库服务信息跟info节点数据是否一致，不一致返回false 根据info节点设置服务信息
	 * 
	 * @param serverInfo
	 * @param serverInfoDto
	 */
	public boolean resetServerInfo(ServerInfo serverInfo,
			ServerInfoDto serverInfoDto) {
		boolean save = true;
		try {
			if (serverInfo != null && serverInfoDto != null
				&& (!serverInfoDto.getOrgCode().equals(serverInfo.getOrgCode())
						|| !serverInfoDto.getServerName().equals(
								serverInfo.getServerName())
						|| (serverInfoDto.getCascadeDomain() != null && !serverInfoDto
								.getCascadeDomain().equals(
										serverInfo.getCascadeDomain())))) {
				save = false;
			}
		} catch (Exception e) {
			save = false;
		} finally {
			if (serverInfoDto != null && serverInfo != null) {
				serverInfo.setOrgCode(serverInfoDto.getOrgCode());
				serverInfo.setServerName(serverInfoDto.getServerName());
				serverInfo.setOrgId(serverInfoDto.getOrgId());
				serverInfo.setCascadeDomain(serverInfoDto.getCascadeDomain());
				serverInfo.setOrgName(serverInfoDto.getOrgName());
				serverInfo.setVersion(serverInfoDto.getVersion());
			}
		}

		return save;
	}

	/**
	 * 设置域节点数据，数据为当前服务ip
	 */
	protected void createAndSetRegionNode() {
		String path = ZookeeperUtil.SEPARATOR + region;
		CuratorClient client = ZookeeperUtil.getCuratorClient();
		JSONObject json = null;
		if (client.checkExist(path)) {
			String data = client.getNodeData(path);
			if (StringUtil.isNotBlank(data)) {
				try {
					json = JSON.parseObject(data);
				} catch (Exception e) {
					logger.error("解析域节点数据异常", e);
				}
			}
		}

		if (json == null) {
			json = new JSONObject();
		}

		json.put(AddressUtil.getLocalAddress(), new Date().toString());

		client.createOrUpdatePath(path, json.toJSONString());
	}

	private void initCommonInfoNode() {
		CuratorClient curator = ZookeeperUtil.getCuratorClient();
		// 初始公有信息节点
		try {
			if (!curator.checkExist(commonInfoPath)) {
				CommonServerInfo commoninfo = new CommonServerInfo();
				commoninfo.setCascadeDomain("");
				commoninfo.setOrgId("");
				commoninfo.setOrgName("");
				curator.createNode(commonInfoPath,
						JSON.toJSONString(commoninfo), CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			logger.error("创建公有信息节点异常", e);
		}
	}
	
	/**
	 * 服务注册处理
	 * @param id
	 * @param serverType
	 * @param path
	 * @param data
	 * @param pathArray
	 */
	private void serverAdd(String id, String serverType, String path, String data, String[] pathArray){
		ServerInfo serverInfo = serverInfoRepsotory.findOne(id);
		if (serverInfo == null) {
			serverInfo = new ServerInfo();
		}
		String changePath = path.substring(path.lastIndexOf('/'),
				path.length());
		String entranceJson = "";
		String ipData = "";

		// 服务上线处理
		if (ZookeeperUtil.ONLINE.equals(changePath)) {
			serverOnline(id, serverType, data);
		}

		// info 节点监听操作
		if (ZookeeperUtil.INFO.equals(changePath)) {
			serverInfoNodeAdd(id, serverType, path, pathArray, serverInfo);
		}
		// ip 节点监听操作
		if (ZookeeperUtil.IP.equals(changePath)) {
			if (!"".equals(ZookeeperUtil.getCuratorClient().getNodeData(
					path))) {
				ipData = ZookeeperUtil.getCuratorClient().getNodeData(path);
			}
			serverRelationServerice.ipChange(id, ipData);
		}
		// entrance 节点监听操作
		if (ZookeeperUtil.ENTRANCE.equals(changePath)) {
			if (!"".equals(ZookeeperUtil.getCuratorClient().getNodeData(
					path))) {
				entranceJson = ZookeeperUtil.getCuratorClient()
						.getNodeData(path);
			}
			serverRelationServerice.entranceChange(serverType, id,
					entranceJson);
			// 服务注册入口信息，为其他服务配置流向
			logger.info("服务{}入口注册，为其他服务配置流向", id);
			if (StringUtils.isNotBlank(entranceJson)) {
				serverRelationServerice.configRelation(serverType);
			}
		}
	}
	
	/**
	 * 服务在线节点注册处理
	 * @param id
	 * @param serverType
	 * @param data
	 */
	private void serverOnline(String id, String serverType, String data){
		ServerInfo dbServerInfo = serverInfoRepsotory.findOne(id);
		if (dbServerInfo == null) {
			dbServerInfo = new ServerInfo();
			dbServerInfo.setId(id);
			dbServerInfo.setServerType(serverType);
		}

		boolean platformConfig = ZookeeperUtil.ONLINEDATA.equals(data);

		if (!platformConfig) {
			dbServerInfo.setConfingFalg(ConfigFlagEnum.REGISTER
					.getFlag());
		} else {
			dbServerInfo
					.setConfingFalg(ConfigFlagEnum.REMOTE.getFlag());
		}
		dbServerInfo.setOnlineStatus(OnlineStatusEnum.ONLINE
				.getStatus());
		serverInfoRepsotory.save(dbServerInfo);

		// 一键配置流向
		List<String> serverTypes = new ArrayList<>();
		serverTypes.add(serverType);
		List<String> serverIds = new ArrayList<>();
		serverIds.add(id);
		logger.info("服务{}上线，开始自动配置流向", id);
		serverRelationServerice.configRelationByTemple(serverTypes,
				serverIds);

		serverRelationServerice.serverOnOffLine(id,
				OnlineStatusEnum.ONLINE.getStatus());
	}
	
	/**
	 * 服务info节点新增监听处理
	 * @param id
	 * @param serverType
	 * @param path
	 * @param pathArray
	 * @param serverInfo
	 */
	private void serverInfoNodeAdd(String id, String serverType, String path, String[] pathArray, ServerInfo serverInfo){
		serverInfo.setId(id);
		serverInfo.setServerType(serverType);
		serverInfo.setStatus("0");
		String onlinePath = ZookeeperUtil.SEPARATOR + region
				+ ZookeeperUtil.SERVERS + ZookeeperUtil.SEPARATOR
				+ serverType + ZookeeperUtil.INSTANCE
				+ ZookeeperUtil.SEPARATOR + id + ZookeeperUtil.ONLINE;
		if (ZookeeperUtil.getCuratorClient().checkExist(onlinePath)) {
			if (ZookeeperUtil.ONLINEDATA.equals(ZookeeperUtil
					.getCuratorClient().getNodeData(onlinePath))) {
				serverInfo.setConfingFalg(ConfigFlagEnum.REMOTE
						.getFlag());
			} else {
				serverInfo.setConfingFalg(ConfigFlagEnum.REGISTER
						.getFlag());
			}
			serverInfo.setOnlineStatus(OnlineStatusEnum.ONLINE
					.getStatus());
		} else {
			serverInfo
					.setConfingFalg(ConfigFlagEnum.REGISTER.getFlag());
			serverInfo.setOnlineStatus(OnlineStatusEnum.OFFLINE
					.getStatus());
		}

		serverInfo.setDomain(pathArray[1]);
		ServerInfoDto serverInfoDto = null;
		if (!"".equals(ZookeeperUtil.getCuratorClient().getNodeData(
				path))) {
			serverInfoDto = JSON.parseObject(ZookeeperUtil
					.getCuratorClient().getNodeData(path),
					ServerInfoDto.class);
		} else {
			serverInfoDto = new ServerInfoDto();
		}
		// 更新服务公有配置信息
		updateCommonServerInfo(serverInfoDto, path);
		String ipPath = path.substring(0, path.indexOf(id)) + id
				+ ZookeeperUtil.IP;
		if (ZookeeperUtil.getCuratorClient().checkExist(ipPath)) {
			AddressDto dto = JSON.parseObject(ZookeeperUtil
					.getCuratorClient().getNodeData(ipPath),
					AddressDto.class);
			if (dto != null) {
				serverInfo.setServerIp(dto.getAddress());
				serverInfo.setAddressType(dto.getAddressType());
			}
		}
		resetServerInfo(serverInfo, serverInfoDto);
		serverInfoRepsotory.save(serverInfo);
	}
	
	/**
	 * 服务信息更新处理
	 * @param id
	 * @param serverType
	 * @param path
	 * @param data
	 * @param pathArray
	 */
	private void serverUpdate(String id, String serverType, String path, String data){
		// info 更新时的监听操作
		if (ZookeeperUtil.INFO.equals(path.substring(path.lastIndexOf('/'),
				path.length()))) {
			ServerInfo serverInfo = serverInfoRepsotory.findOne(id);
			ServerInfoDto serverInfoDto = null;
			if (StringUtils.isNotBlank(data)) {
				serverInfoDto = JSON.parseObject(data, ServerInfoDto.class);
			} else {
				serverInfoDto = new ServerInfoDto();
			}
			// 更新服务公有配置信息
			updateCommonServerInfo(serverInfoDto, path);
			if (!resetServerInfo(serverInfo, serverInfoDto)) {
				serverInfoRepsotory.save(serverInfo);
			}
		}

		// ip 改变
		if (ZookeeperUtil.IP.equals(path.substring(path.lastIndexOf('/'),
				path.length()))) {
			// id data
			logger.info("服务{}地址变更：{}，为其他服务配置流向", id, data);
			serverRelationServerice.ipChange(id, data);
		}

		// entrance 服务入口改变
		if (ZookeeperUtil.ENTRANCE.equals(path.substring(
				path.lastIndexOf('/'), path.length()))) {
			serverRelationServerice.entranceChange(serverType, id, data);
			// 服务注册入口信息，为其他服务配置流向
			logger.info("服务{}入口变更，为其他服务配置流向", id);
			if (StringUtils.isNotBlank(data)) {
				serverRelationServerice.configRelation(serverType);
			}
		}
	}
}
