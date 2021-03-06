package com.hy.zookeeper.config.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.common.PageInfo;
import com.hy.zookeeper.config.dao.RelationsRepsotory;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.dao.ServerTypeRepsotory;
import com.hy.zookeeper.config.dto.AddressDto;
import com.hy.zookeeper.config.dto.CommonServerInfo;
import com.hy.zookeeper.config.dto.EntranceDto;
import com.hy.zookeeper.config.dto.ServerInfoDto;
import com.hy.zookeeper.config.entity.RunningConfig;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.entity.PlatformServerType;
import com.hy.zookeeper.config.enums.ConfigFlagEnum;
import com.hy.zookeeper.config.enums.OnlineStatusEnum;
import com.hy.zookeeper.config.enums.ResultKeyConst;
import com.hy.zookeeper.config.service.INodeDataService;
import com.hy.zookeeper.config.service.IServerInfoService;
import com.hy.zookeeper.config.service.IServerRelationService;
import com.hy.zookeeper.config.util.UUIDTOOL;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@Service
@Transactional(value="jpaTransactionManager")
public class ServerInfoServiceImpl implements IServerInfoService {

	protected static final Logger logger = LoggerFactory.getLogger(ServerInfoServiceImpl.class);

	@Autowired
	private ServerInfoRepsotory serverInfoRepsotory;

	@Autowired
	private ServerTypeRepsotory serverTypeRepsotory;

	@Autowired
	private IServerRelationService serverRelationServerice;

	@Resource
	private RelationsRepsotory relationsRepsotory;

	@Resource
	private ServerEntranceRepository entranceRepository;

	@Resource
	private INodeDataService nodeDataService;

	@Value("${root.region}")
	private String region;
	@Value("${common.info.path}")
	private String commonInfoPath;

	@Override
	public Map<String,Object> findAllPage(int page, int row, ServerInfo info) {
		Map<String,Object> result = new HashMap<>();
		//serverTypeRepsotory.
		PageInfo pageInfo = new PageInfo();
		String sql = "SELECT * FROM PLATFORM_SERVER_INFO where 1=1 ";
		if(info != null){
			if(StringUtils.isNotBlank(info.getServerType())){
				sql += " AND server_type='" + info.getServerType() + "'";
			}
			if(StringUtils.isNotBlank(info.getServerName())){
				sql += " AND server_name like '%" + info.getServerName() + "%'";
			}
			if(StringUtils.isNotBlank(info.getServerIp())){
				sql += " AND address_value like '%" + info.getServerIp() + "%'";
			}
			if(StringUtils.isNotBlank(info.getId())){
				sql += " AND id like '%" + info.getId() + "%'";
			}
		}
		sql += " ORDER BY confing_falg ASC ";
		pageInfo.setPage(page);
		pageInfo.setRows(row);
		List<ServerInfo> serverInfoList = serverInfoRepsotory.findPageBySQL(sql, pageInfo);

		result.put("rows", serverInfoList);
		int total = (int) (pageInfo.getTotal()%row==0?pageInfo.getTotal()/row:pageInfo.getTotal()/row+1);
		result.put("total", total);
		result.put("records", pageInfo.getTotal());


		return result;
	}

	@Override
	public boolean saveServer(ServerInfo serverInfo) {
		try {
			if("".equals(serverInfo.getId())){
				serverInfo.setId(UUIDTOOL.getuuid(32));
				serverInfo.setStatus("0");
			}

			CommonServerInfo commonServerInfo = getCommonServerInfo();
			serverInfo.setCascadeDomain(commonServerInfo.getCascadeDomain());
			serverInfo.setOrgId(commonServerInfo.getOrgId());
			serverInfo.setOrgName(commonServerInfo.getOrgName());

			serverInfoRepsotory.saveAndFlush(serverInfo);
			createNode(serverInfo,true);
			return true;
		} catch (Exception e) {
			logger.error("?????????????????????"+serverInfo);
		}
		return false;
	}

	@Override
	public ServerInfo getServerById(String id) {

		return serverInfoRepsotory.findOne(id);
	}

	@Override
	public Map<String,Object> deleteServer(String id) {
		Map<String,Object> result = new HashMap<>();
		CuratorClient client = ZookeeperUtil.getCuratorClient();
		ServerInfo serverInfo = serverInfoRepsotory.findOne(id);
		String serverIdPath = getServerIdPath(serverInfo);
		String onlinePath =  serverIdPath + ZookeeperUtil.ONLINE;
		if(client.checkExist(onlinePath) && !ZookeeperUtil.ONLINEDATA.equals(client.getNodeData(onlinePath))){
			result.put(ResultKeyConst.SUCESS_KEY, false);
			result.put("msg", "???????????????????????????");
		}else{
			serverInfoRepsotory.delete(id);
			serverRelationServerice.deleteBySrcServerId(id);
			serverRelationServerice.entranceChange(serverInfo.getServerType(), id, "");

			// ??????entrance????????????????????????????????????entrance???????????????????????????online????????????????????????
			String entrancePath = serverIdPath + ZookeeperUtil.ENTRANCE;
			if(client.checkExist(entrancePath)){
				client.deleteNode(entrancePath);
			}

			createNode(serverInfo,false);
			result.put(ResultKeyConst.SUCESS_KEY, true);
		}
		return result;
	}

	/**
	 * ????????????????????????zookeeper??????
	 * @param serverInfo ????????????
	 * @param isDel ??????????????????  true ?????? false ??????
	 * @return
	 */
	public boolean createNode(ServerInfo serverInfo,boolean isDel){
		String path = ZookeeperUtil.SEPARATOR+region+ZookeeperUtil.SERVERS;

		try {
			CommonServerInfo commoninfo = this.getCommonServerInfo();
			CuratorClient curator = ZookeeperUtil.getCuratorClient();
			if(isDel){
				curator.createIfPathNotExist(path+ZookeeperUtil.SEPARATOR+serverInfo.getServerType(), "");

				String serverTypeChildren = "";
				//????????????id??????
				serverTypeChildren = path+ZookeeperUtil.SEPARATOR+serverInfo.getServerType()+ZookeeperUtil.INSTANCE+ZookeeperUtil.SEPARATOR+serverInfo.getId();

				String onlinePath = serverTypeChildren + ZookeeperUtil.ONLINE;
				//????????????????????????
				if(ConfigFlagEnum.REMOTE.getFlag().equals(serverInfo.getConfingFalg())){
					// ?????????????????????????????????????????????
					curator.createIfPathNotExist(onlinePath, ZookeeperUtil.ONLINEDATA);
				}else{
					// ???????????????????????????????????????????????????online??????
					if(curator.checkExist(onlinePath)
							&& ZookeeperUtil.ONLINEDATA.equals(curator.getNodeData(onlinePath))){
						curator.deleteNode(onlinePath);
						serverInfo.setOnlineStatus(OnlineStatusEnum.OFFLINE.getStatus());
						serverInfoRepsotory.save(serverInfo);
					}
				}

				curator.createIfPathNotExist(path+ZookeeperUtil.SEPARATOR+serverInfo.getServerType()+ZookeeperUtil.INSTANCE, "");

				//??????id??????  ?????????????????????
				curator.createIfPathNotExist(path+ZookeeperUtil.SEPARATOR+serverInfo.getServerType()+ZookeeperUtil.INSTANCE+ZookeeperUtil.SEPARATOR+serverInfo.getId(), "");

				//??????id??????????????????
				AddressDto address = new AddressDto(serverInfo);
				ServerInfoDto infoDto = new ServerInfoDto();
				infoDto.setOrgCode(serverInfo.getOrgCode());
				infoDto.setServerName(serverInfo.getServerName());
				infoDto.setVersion(serverInfo.getVersion());
				infoDto.setCascadeDomain(commoninfo.getCascadeDomain());
				infoDto.setOrgId(commoninfo.getOrgId());
				infoDto.setOrgName(commoninfo.getOrgName());
				if(!curator.checkExist(serverTypeChildren+ZookeeperUtil.INFO)){
				    curator.createNode(serverTypeChildren+ZookeeperUtil.INFO, JSON.toJSONString(infoDto), CreateMode.PERSISTENT);

				    curator.createNode(serverTypeChildren+ZookeeperUtil.IP, JSON.toJSONString(address), CreateMode.PERSISTENT);

				    List<EntranceDto> entranceDtos = getEntranceDtoList(serverInfo.getId());
				    String entrancePath = serverTypeChildren+ZookeeperUtil.ENTRANCE;
				    curator.createOrUpdatePath(entrancePath, JSON.toJSONString(entranceDtos));
				}else{
					curator.updateNodeData(serverTypeChildren+ZookeeperUtil.INFO, JSON.toJSONString(infoDto));
					curator.updateNodeData(serverTypeChildren+ZookeeperUtil.IP, JSON.toJSONString(address));
				}
			}else{
				curator.deleteNode(path+ZookeeperUtil.SEPARATOR+serverInfo.getServerType()+ZookeeperUtil.INSTANCE+ZookeeperUtil.SEPARATOR+serverInfo.getId());
			}

		} catch (Exception e) {
			logger.error("???????????????????????????id:{}", serverInfo.getId(), e);
		}



		return true;
	}

	/**
	 * ???????????? ?????????????????????????????????
	 * @param zk zookeeper ??????
	 * @param path ???????????????
	 * @throws Exception
	 */
    public void delNode(CuratorClient curator,String path) throws Exception {
    	if(curator.checkExist(path)){
    		//????????????????????????
            List<String> children = curator.getClildrenList(path);
            for (String pathCd : children) {
                //???????????????????????????????????????
                String newPath = "";
                //????????????,????????????????????????
                if (path.equals("/")) {
                    newPath = ZookeeperUtil.SEPARATOR + pathCd;
                } else {
                    newPath = path + ZookeeperUtil.SEPARATOR + pathCd;
                }
                delNode(curator,newPath);

            }
            //????????????,?????????zookeeper????????? /??????
            if (path != null && !path.trim().startsWith("/zookeeper") && !path.trim().equals("/")) {
                curator.deleteNode(path);
            }
    	}
    }

	@Override
	public int updateStatus(String id,String status,String cumlum) {
		String sql = "UPDATE PLATFORM_SERVER_INFO set "+cumlum+" = '"+status+"' where id = '"+id+"' ";
		if(serverInfoRepsotory.updateBySql(sql)>0){
			createNode(serverInfoRepsotory.getOne(id),true);
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public Map<String, Object> getServerSelectData() {
		Map<String, Object> result =  new HashMap<>();
		Map<String, List<String>> typeIdsMap =  new HashMap<>();
		Map<String, String> typeNameMap =  new HashMap<>();
		Map<String, String> idNameMap =  new HashMap<>();
		List<ServerInfo> list = serverInfoRepsotory.findAll();
		List<PlatformServerType> typeList = serverTypeRepsotory.findAll();
		Map<String, String> typesMap =  new HashMap<>();
		for(PlatformServerType t : typeList){
			typesMap.put(t.getServerType(), t.getServerTypeName());
		}
		for(ServerInfo s : list){
			String serverType = s.getServerType();
			List<String> ids = typeIdsMap.get(serverType);
			if(ids == null){
				ids = new ArrayList<>();
			}
			ids.add(s.getId());
			typeIdsMap.put(serverType, ids);
			typeNameMap.put(serverType, typesMap.get(serverType));
			idNameMap.put(s.getId(), s.getServerName());
		}
		result.put("typeIdsMap", typeIdsMap);
		result.put("typeNameMap", typeNameMap);
		result.put("idNameMap", idNameMap);
		return result;
	}

	@Override
	public List<ServerInfo> findAll() {
		return serverInfoRepsotory.findAll();
	}

	@Override
	public List<RunningConfig> getRunningConfig(String serverId) {
		CuratorClient curator = ZookeeperUtil.getCuratorClient();
		ServerInfo serverInfo = getServerById(serverId);
		String path = ZookeeperUtil.SEPARATOR + region + ZookeeperUtil.SERVERS + ZookeeperUtil.SEPARATOR
				+ serverInfo.getServerType() + ZookeeperUtil.INSTANCE + ZookeeperUtil.SEPARATOR
				+ serverInfo.getId() + ZookeeperUtil.RUNNINGCONFIG;
		if(!curator.checkExist(path)){
			try {
				curator.createNode(path, "", CreateMode.PERSISTENT);
			} catch (Exception e) {
				logger.error("????????????????????????????????????,serverId:{}", serverId, e);
			}
			return new ArrayList<>();
		}else{
			String data = curator.getNodeData(path);
			List<RunningConfig> list = JSON.parseArray(data, RunningConfig.class);
			if(list == null){
				list = new ArrayList<>();
			}
			return list;
		}
	}

	@Override
	public void saveRunningConfig(String configData, String serverId) {
		CuratorClient curator = ZookeeperUtil.getCuratorClient();
		ServerInfo serverInfo = getServerById(serverId);
		String path = ZookeeperUtil.SEPARATOR + region + ZookeeperUtil.SERVERS + ZookeeperUtil.SEPARATOR
				+ serverInfo.getServerType() + ZookeeperUtil.INSTANCE + ZookeeperUtil.SEPARATOR
				+ serverInfo.getId() + ZookeeperUtil.RUNNINGCONFIG;
		curator.updateNodeData(path, configData);
	}

	@Override
	public void cleanAndRegistServerInDb(String exclusDelType, CuratorClient curator) {
		relationsRepsotory.deleteAll();
		// ???????????????????????????
		List<ServerInfo> serverInfos = serverInfoRepsotory.findByConfingFalg(ConfigFlagEnum.REMOTE.getFlag());
		if(serverInfos != null && !serverInfos.isEmpty()){
			List<String> ids = new ArrayList<>();
			for(ServerInfo s : serverInfos){
				ids.add(s.getId());
			}
			// ??????????????????????????????
			serverInfoRepsotory.deleteByIdNotIn(ids);
			// ??????????????????????????????
			entranceRepository.deleteByServerIdNotIn(ids);
			// ??????????????????????????????zookeeper
			registServerInDb(serverInfos, curator);
		}
	}

	/**
	 * ???????????????zookeeper?????????
	 * @param serverInfos
	 */
	public void registServerInDb(List<ServerInfo> serverInfos, CuratorClient curator){
		if(serverInfos != null){
			for(ServerInfo serverInfo : serverInfos){
				String serverId = serverInfo.getId();
				String basePath = ZookeeperUtil.SEPARATOR + region + ZookeeperUtil.SERVERS + ZookeeperUtil.SEPARATOR
						+ serverInfo.getServerType() + ZookeeperUtil.INSTANCE + ZookeeperUtil.SEPARATOR
						+ serverInfo.getId();
				try{
					// ??????online??????
					String onlinePath = basePath + ZookeeperUtil.ONLINE;
					curator.deleteNode(onlinePath);
					/**
					 *  ??????????????????online????????????platformConfig???????????????????????????????????????
					 */
					curator.createNode(onlinePath, ZookeeperUtil.ONLINEDATA, CreateMode.PERSISTENT);

					//??????info
					String infoPath = basePath + ZookeeperUtil.INFO;
					ServerInfoDto infoDto = new ServerInfoDto();
					infoDto.setOrgCode(serverInfo.getOrgCode());
					infoDto.setServerName(serverInfo.getServerName());
					curator.createOrUpdatePath(infoPath, JSON.toJSONString(infoDto));

					//??????address
					AddressDto addressDto = new AddressDto();
					addressDto.setAddressType(serverInfo.getAddressType());
					addressDto.setAddress(serverInfo.getServerIp());
					String addressPath = basePath + ZookeeperUtil.IP;
					curator.createOrUpdatePath(addressPath, JSON.toJSONString(addressDto));

					//??????entrance
					String entrancePath = basePath + ZookeeperUtil.ENTRANCE;
					List<EntranceDto> entranceDtos = new ArrayList<>();
					List<ServerEntrance> entrances = entranceRepository.findByServerId(serverId);
					if(entrances != null){
						for(ServerEntrance e : entrances){
							entranceDtos.add(createEntranceDto(e));
						}
					}

					curator.createOrUpdatePath(entrancePath, JSON.toJSONString(entranceDtos));

					//??????relation
					String relationPath = basePath + ZookeeperUtil.RELATION;
					curator.createIfPathNotExist(relationPath, "");
				}catch(Exception e){
					logger.error("??????????????????????????????serverId???{}", serverId, e);
				}

			}
		}

	}

	public EntranceDto createEntranceDto(ServerEntrance entrance){
		return EntranceDto.getDto(entrance);
	}

	@Override
	public CommonServerInfo getCommonServerInfo() {
		try{
			CuratorClient curator = ZookeeperUtil.getCuratorClient();
			if(curator.checkExist(commonInfoPath)){
				String infoStr =  curator.getNodeData(commonInfoPath);
				return JSON.parseObject(infoStr, CommonServerInfo.class);
			}else {
				logger.warn("???????????????????????????");
			}
		}catch(Exception e){
			logger.error("????????????????????????", e);
		}
		return new CommonServerInfo();
	}

	@Override
	public Map<String, Object> saveCommonServerInfo(CommonServerInfo info) {
		Map<String, Object> result = new HashMap<>();
		try{
			String sql = " UPDATE PLATFORM_SERVER_INFO SET cascade_domain='" + info.getCascadeDomain()
					+ "', org_id='" + info.getOrgId() + "', org_name='" + info.getOrgName() + "'";
			serverInfoRepsotory.executeSQL(sql);
			CuratorClient curator = ZookeeperUtil.getCuratorClient();
			if(curator.checkExist(commonInfoPath)){
				curator.updateNodeData(commonInfoPath, JSON.toJSONString(info));
			}else{
				curator.createNode(commonInfoPath, JSON.toJSONString(info), CreateMode.PERSISTENT);
			}
			editServerInfo(info);
			result.put("success", true);
		}catch(Exception e){
			logger.error("????????????????????????", e);
			result.put("success", false);
			result.put("msg", e.getMessage());
		}
		return result;
	}

	/**
	 * ???????????????????????????????????????????????????
	 * @param commonInfo
	 */
	@Override
	public void editServerInfo(CommonServerInfo commonInfo){
		CuratorClient curator = ZookeeperUtil.getCuratorClient();
		List<ServerInfo> serverInfos = serverInfoRepsotory.findAll();
		for(ServerInfo s : serverInfos){
			String path = ZookeeperUtil.SEPARATOR + region + "/servers/" + s.getServerType() + "/instance/" + s.getId() + "/info";
			if(curator.checkExist(path)){
				String data = curator.getNodeData(path);
				if(StringUtils.isNotBlank(data)){
					ServerInfoDto info = JSON.parseObject(data, ServerInfoDto.class);
					info.setCascadeDomain(commonInfo.getCascadeDomain());
					info.setOrgId(commonInfo.getOrgId());
					info.setOrgName(commonInfo.getOrgName());
					curator.updateNodeData(path, JSON.toJSONString(info));
				}else{
					curator.updateNodeData(path, JSON.toJSONString(commonInfo));
				}
			}
		}
	}

	@Override
	public Map<String, Object> getServerDatial(String serverId) {
		Map<String, Object> result = new HashMap<>();
		ServerInfo serverInfo = serverInfoRepsotory.findOne(serverId);
		CuratorClient curator = ZookeeperUtil.getCuratorClient();

		String rolePath = ZookeeperUtil.SEPARATOR+region+ZookeeperUtil.SERVERS+
				ZookeeperUtil.SEPARATOR+serverInfo.getServerType()+ZookeeperUtil.INSTANCE
				+ ZookeeperUtil.SEPARATOR+serverId+ZookeeperUtil.ROLE;
		if(curator.checkExist(rolePath)){
			String role = curator.getNodeData(rolePath);
			result.put("role", StringUtils.isNotBlank(role)?role:"0");
		}
		result.put("serverInfo", serverInfo);
		return result;
	}

	@Override
	public void serverOffline(String id, Integer status) {
		ServerInfo serverInfo = serverInfoRepsotory.findOne(id);
		if(serverInfo != null){
			if(OnlineStatusEnum.OFFLINE.getStatus().equals(status)) {
				serverInfo.setConfingFalg(ConfigFlagEnum.REGISTER.getFlag());
			}
			serverInfo.setOnlineStatus(status);
			serverInfoRepsotory.save(serverInfo);
		}
	}

	@Override
	public int delOfflineServer() {
		List<ServerInfo> serverInfos = serverInfoRepsotory.findByOnlineStatus(
				OnlineStatusEnum.OFFLINE.getStatus());
		return deleteOfflineServers(serverInfos);
	}

	@Override
	public int deleteServers(String serverIds) {
		List<String> idList = JSON.parseArray(serverIds, String.class);
		List<ServerInfo> serverInfos = serverInfoRepsotory.findByIdIn(idList);
		return deleteOfflineServers(serverInfos);
	}

	private int deleteOfflineServers(List<ServerInfo> serverInfos){
		int count = 0;
		if(serverInfos != null){
			CuratorClient client = ZookeeperUtil.getCuratorClient();
			for(ServerInfo serverInfo : serverInfos){
				String path = getOnlinePath(serverInfo);
				if(!client.checkExist(path) && !ConfigFlagEnum.REMOTE.getFlag()
						.equals(serverInfo.getConfingFalg())){
					count++;
					String id = serverInfo.getId();
					serverInfoRepsotory.delete(id);
					serverRelationServerice.deleteBySrcServerId(id);
					serverRelationServerice.entranceChange(serverInfo.getServerType(), id, "");
					createNode(serverInfo,false);
				}
			}
		}
		return count;
	}

	private String getOnlinePath(ServerInfo serverInfo){
		return getServerIdPath(serverInfo) + ZookeeperUtil.ONLINE;
	}

	private String getServerIdPath(ServerInfo serverInfo){
		return ZookeeperUtil.SEPARATOR + region + ZookeeperUtil.SERVERS
				+ ZookeeperUtil.SEPARATOR + serverInfo.getServerType()
				+ ZookeeperUtil.INSTANCE + ZookeeperUtil.SEPARATOR
				+ serverInfo.getId();
	}

	private List<EntranceDto> getEntranceDtoList(String serverId){
		List<ServerEntrance> entranceList = entranceRepository.findByServerId(serverId);
	    List<EntranceDto> entranceDtos = new ArrayList<>();
	    if(entranceList != null){
	    	for(ServerEntrance e : entranceList ){
				entranceDtos.add(EntranceDto.getDto(e));
			}
	    }
	    return entranceDtos;
	}

	@Override
	public void registeThirdPartyServer() {
		List<ServerInfo> serverInfos = serverInfoRepsotory.findByConfingFalg(ConfigFlagEnum.REMOTE.getFlag());
		// ??????????????????????????????zookeeper
		registServerInDb(serverInfos, ZookeeperUtil.getCuratorClient());

		nodeDataService.syncNodeData();
	}

}
