package com.hy.zookeeper.config.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.curator.framework.CuratorFramework;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.common.PageInfo;
import com.hy.zookeeper.config.dao.RelationTempletRepository;
import com.hy.zookeeper.config.dao.RelationsRepsotory;
import com.hy.zookeeper.config.dao.ServerEntranceRepository;
import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.dao.ServerTypeRepsotory;
import com.hy.zookeeper.config.dto.AddressDto;
import com.hy.zookeeper.config.dto.EntranceDto;
import com.hy.zookeeper.config.dto.ResultBean;
import com.hy.zookeeper.config.entity.RelationTemplet;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.entity.ServerRelation;
import com.hy.zookeeper.config.enums.CommonConst;
import com.hy.zookeeper.config.enums.ResultKeyConst;
import com.hy.zookeeper.config.model.LinkDataModel;
import com.hy.zookeeper.config.model.MapDataModel;
import com.hy.zookeeper.config.model.NodeDataModel;
import com.hy.zookeeper.config.model.RelationTreeModel;
import com.hy.zookeeper.config.service.IFunctionCodesService;
import com.hy.zookeeper.config.service.IServerRelationService;
import com.hy.zookeeper.config.util.StringUtil;
import com.hy.zookeeper.config.util.UUIDTOOL;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@Transactional
@Service("serverRelationService")
public class ServerRelationServiceImpl implements IServerRelationService{

	protected static final Logger logger = LoggerFactory.getLogger(ServerRelationServiceImpl.class);

	@Value("${root.region}")
	private String region;

	private String split1 = "_HsplitY_"; // ????????????1??????????????????????????????
	private String split2 = "_HlinetoY_"; // ????????????2???????????????????????????
//	private String providerMarker = "_HpY_" // ??????????????????
//	private String consumerMarker = "_HcY_" // ??????????????????

	@Resource
	private RelationsRepsotory relationsRepsotory;

	@Resource
	private ServerEntranceRepository entranceRepository;

	@Resource
	private ServerInfoRepsotory serverInfoRepsotory;

	@Resource
	private RelationTempletRepository templetRepository;

	@Resource
	private ServerTypeRepsotory serverTypeRepsotory;

	@Resource
	private IFunctionCodesService functionCodesService;

	@Override
	public List<ServerRelation> getRelationByServerIds(List<String> serverIds) {
		return relationsRepsotory.findBySrcServerIdIn(serverIds);
	}

	@Override
	public boolean ipChange(String serverId, String address) {
		/**
		 * 1?????????serverId???????????????????????????????????????id?????????
		 * 2?????????????????????????????????????????????????????????????????????
		 * 3???????????????????????????????????????zookeeper?????????
		 */
		try{
			AddressDto dto = JSON.parseObject(address, AddressDto.class);
			String ip = "";
			if(dto != null){
				ip = dto.getAddress();
				ServerInfo serverInfo = serverInfoRepsotory.findOne(serverId);
				if(serverInfo == null){
					serverInfo = new ServerInfo();
					serverInfo.setId(serverId);
				}
				if(!ip.equals(serverInfo.getServerIp())){
					serverInfo.setServerIp(ip);
					serverInfo.setAddressType(dto.getAddressType());
					serverInfoRepsotory.save(serverInfo);
				}
			}

			List<ServerRelation> relations = relationsRepsotory.findByDestServerId(serverId);

			if(relations != null && !relations.isEmpty()){
				// ?????????????????????????????????????????????   key:serverId  value:path
				Map<String, String> srcServerMap = new HashMap<>();
				for(ServerRelation r : relations){
					// ??????ip?????????ip?????????
					if(!ip.equals(r.getDestIp())){
						r.setDestIp(ip);
						String srcServerId = r.getSrcServerId();
						srcServerMap.putIfAbsent(srcServerId, getRelationPath(r));
					}
				}
				relationsRepsotory.save(relations);

				// ???????????????
				if(!srcServerMap.isEmpty()){
					editRelationNode(srcServerMap);
				}
			}

			functionCodesService.updateFunctionCodes(serverId);
			return true;
		}catch(Exception e){
			logger.error("ip?????????????????????serverId:{}, address:{}", serverId, address, e);
			return false;
		}
	}

	@Override
	public boolean entranceChange(String serverType, String serverId,
			String entranceJsonStr) {
		try{
			functionCodesService.compareFcsAndUpdateFunctionCodes(serverId, entranceJsonStr);

			// ????????????serverId?????????????????????
			entranceRepository.deleteByServerId(serverId);

			// ????????????????????????
			List<ServerEntrance> newList = new ArrayList<>();

			// ?????????-??????DTO??????
			Map<String, EntranceDto> fcEntranceMap = new HashMap<>();
			List<EntranceDto> entranceList = new ArrayList<>();
			if(StringUtils.isNotBlank(entranceJsonStr)){
				 entranceList = JSON.parseArray(entranceJsonStr, EntranceDto.class);
			}
			for(EntranceDto e : entranceList){
				for(String fc : e.getFcs()){
					fcEntranceMap.put(fc, e);
				}
				// ????????????????????????
				ServerEntrance entity = new ServerEntrance(serverType, serverId, e);
				newList.add(entity);
			}

			if(!newList.isEmpty()){
				entranceRepository.save(newList);
			}

			List<ServerRelation> relations = relationsRepsotory.findByDestServerId(serverId);

			// ????????????????????????????????????
			List<ServerRelation> delList =  new ArrayList<>();
			// ????????????????????????????????????
			List<ServerRelation> updateList =  new ArrayList<>();

			if(relations == null || relations.isEmpty()){
				return true;
			}

			// ??????ID-??????      ????????????????????????????????????????????????
			Map<String, String> serverPathMap = new HashMap<>();
			for(ServerRelation r : relations){
				EntranceDto entrance = fcEntranceMap.get(r.getDestProviderFc());
				String srcServerId = r.getSrcServerId();
				if(entrance != null){
					r.setDestPort(entrance.getPort());
					r.setDestProtocol(entrance.getProtocol());
					r.setDestUrl(entrance.getUrl());
					r.setUserName(entrance.getUserName());
					r.setPassword(entrance.getPassword());
					updateList.add(r);
				}else{
					delList.add(r);
				}

				serverPathMap.putIfAbsent(srcServerId, getRelationPath(r.getSrcServerType(), srcServerId));
			}

			if(!updateList.isEmpty()){
				relationsRepsotory.save(updateList);
			}

			if(!delList.isEmpty()){
				relationsRepsotory.delete(delList);
			}

			editRelationNode(serverPathMap);

			return true;
		}catch(Exception e){
			logger.error("???????????????????????????serverId:{}", serverId, e);
			return false;
		}
	}

	@Override
	public boolean saveRelation(ServerRelation relation) {
		try{
			ServerEntrance entrance = entranceRepository.findOne(relation.getDestEntranceId());
			ServerInfo destServer = serverInfoRepsotory.findOne(relation.getDestServerId());
			if(StringUtils.isBlank(relation.getId())){
				relation.setId(UUIDTOOL.getuuid(32));
			}
			if(StringUtils.isBlank(destServer.getServerIp())){
				destServer.setServerIp(getServerIp(destServer));
			}
			relation.setDestIp(destServer.getServerIp());
			relation.setDestPort(entrance.getPort());
			relation.setDestProtocol(entrance.getProtocol());
			relation.setDestUrl(entrance.getURL());
			relation.setUserName(entrance.getUserName());
			relation.setPassword(entrance.getPassword());
			relation.setDestOnlineStatus(destServer.getOnlineStatus());
			relationsRepsotory.saveAndFlush(relation);
			editRelationNode(relation.getSrcServerType(), relation.getSrcServerId());
		}catch(Exception e){
			logger.error("????????????????????????", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean batchSave(List<ServerRelation> relationList) {
		try{
			if(relationList != null && !relationList.isEmpty()){
				for(ServerRelation r : relationList){
					r.setId(UUIDTOOL.getuuid(32));
				}
				relationsRepsotory.save(relationList);
			}
			return true;
		}catch(Exception e){
			logger.error("??????????????????????????????", e);
			return false;
		}
	}

	@Override
	public String importRelation(MultipartFile file) {

		List<ServerRelation> serverRelationList = new ArrayList<>();
		if (!file.isEmpty()) {
            try {
                /*
                 * ???????????????????????????????????????????????????????????????????????? ???????????????????????????????????????????????????????????????
                 * d:/files??????????????????????????? ??????
                 * ??????????????????????????????,??????????????????????????????????????????????????????????????????????????????????????? 1?????????????????? 2???????????????
                 * 3???????????????; 4????????????????????????
                 */
            	POIFSFileSystem fs=new POIFSFileSystem(file.getInputStream());
            	HSSFWorkbook wb=new HSSFWorkbook(fs);
            	Sheet sheet = wb.getSheetAt(0);
	        	if(sheet.getPhysicalNumberOfRows() > 1){
	        		  for(int k=1;k<sheet.getPhysicalNumberOfRows();k++ ){
	        			  ServerRelation serverRelation = new ServerRelation();
	        			  Row rows = sheet.getRow(k);
	        			  Cell cell1 = rows.getCell(0);
	        			  serverRelation.setRelationName(cell1.getStringCellValue());
	        			  Cell cell2 = rows.getCell(1);
	        			  serverRelation.setSrcServerType(cell2.getStringCellValue());
	        			  Cell cell3 = rows.getCell(2);
	        			  serverRelation.setSrcServerName(cell3.getStringCellValue());
	        			  Cell cell4 = rows.getCell(3);
	        			  serverRelation.setSrcServerId(cell4.getStringCellValue());
	        			  Cell cell5 = rows.getCell(4);
	        			  serverRelation.setSrcConsumerFc(cell5.getStringCellValue());
	        			  Cell cell6 = rows.getCell(5);
	        			  serverRelation.setDestServerType(cell6.getStringCellValue());
	        			  Cell cell7 = rows.getCell(6);
	        			  serverRelation.setDestServerName(cell7.getStringCellValue());
	        			  Cell cell8 = rows.getCell(7);
	        			  serverRelation.setDestServerId(cell8.getStringCellValue());
	        			  Cell cell9 = rows.getCell(8);
	        			  serverRelation.setDestProviderFc(cell9.getStringCellValue());
	        			  Cell cell10 = rows.getCell(9);
	        			  serverRelation.setDestIp(cell10.getStringCellValue());
	        			  Cell cell11 = rows.getCell(10);
	        			  serverRelation.setDestPort((int)cell11.getNumericCellValue());
	        			  Cell cell12 = rows.getCell(11);
	        			  serverRelation.setDestProtocol(cell12.getStringCellValue());
	        			  Cell cell13 = rows.getCell(12);
	        			  serverRelation.setDestUrl(cell13.getStringCellValue());
	        			  serverRelation.setId(UUIDTOOL.getuuid(32));
	        			  serverRelationList.add(serverRelation);
	        		  }
	        	}
	        	wb.close();
            } catch (IOException e) {
                logger.error("??????????????????", e);
                return "????????????," + e.getMessage();
            }

 			if(!serverRelationList.isEmpty()){
 				Map<String, String> srcServerMap = new HashMap<>();
 				for(ServerRelation r : serverRelationList){
 					String srcServerId = r.getSrcServerId();
 					srcServerMap.putIfAbsent(srcServerId, getRelationPath(r.getSrcServerType(), srcServerId));
 				}
 				relationsRepsotory.save(serverRelationList);

 				editRelationNode(srcServerMap);
 			}
            return "????????????";

        } else {
            return "????????????????????????????????????.";
        }
	}

	@Override
	public ServerRelation getRelationById(String id) {
		return relationsRepsotory.findOne(id);
	}

	public String getServerIp(ServerInfo serverInfo){
		String ip = null;
		String ipPath = ZookeeperUtil.SEPARATOR + region + ZookeeperUtil.SERVERS + ZookeeperUtil.SEPARATOR
				+ serverInfo.getServerType() + ZookeeperUtil.INSTANCE + ZookeeperUtil.SEPARATOR
				+ serverInfo.getId() + ZookeeperUtil.IP;
		try{
			CuratorClient client = ZookeeperUtil.getCuratorClient();
			String ipData = client.getNodeData(ipPath);
			AddressDto dto = JSON.parseObject(ipData, AddressDto.class);
			ip = dto.getAddress();
		}catch(Exception e){
			logger.error("????????????{}ip??????",serverInfo.getId(),e);
		}

		return ip;
	}

	/**
	 * ????????????????????????
	 * @param server
	 * @return
	 */
	public boolean serverOnline(ServerInfo server){
		String path = ZookeeperUtil.SEPARATOR + region + ZookeeperUtil.SERVERS+ZookeeperUtil.SEPARATOR
				+ server.getServerType() + ZookeeperUtil.INSTANCE+ZookeeperUtil.SEPARATOR
				+ server.getId() + "/online";
		try{
			CuratorClient client = ZookeeperUtil.getCuratorClient();
			return client.checkExist(path);
		}catch(Exception e){
			logger.error("??????????????????????????????", e);
		}
		return false;
	}

	@Override
	public boolean configRelationByTemple(List<String> serverTypes, List<String> serverIds){
		return configRelationByTemple(serverTypes, serverIds, null);
	}

	/**
	 *
	 */
	@Override
	public boolean configRelationByTemple(List<String> serverTypes, List<String> serverIds, String destServerType) {
		List<ServerInfo> serverInfos = serverInfoRepsotory.findAll();
		Pair<Map<String, List<ServerInfo>>, Map<String, ServerInfo>> serverInfoPair = getServerTypeAndServerIdMap(serverInfos);
		// key:serverType  ?????????????????????????????????????????????
		Map<String, List<ServerInfo>> serverTypeMap = serverInfoPair.getLeft();
		// key:serverId  
		Map<String, ServerInfo> serverIdMap = serverInfoPair.getRight();

		// ?????????????????????id
		Map<String, String> configIdMap = new HashMap<>();
		for(String s : serverIds){
			configIdMap.put(s, s);
		}

		List<ServerRelation> relationsInDb = relationsRepsotory.findBySrcServerTypeIn(serverTypes);
		// key:srcServerId_srcConsumerFc_destServerId_destProviderFc-value:serverId 
		// ????????????????????????????????????,??????????????????????????????????????????
		Set<String> relationKeySet = new HashSet<>();
		for(ServerRelation r : relationsInDb){
			String rKey = r.getSrcServerId()+"_"+r.getSrcConsumerFc()+"_"+r.getDestServerId()+"_"+r.getDestProviderFc();
			relationKeySet.add(rKey);
		}

		// ???????????????????????????
		List<RelationTemplet> templets;
		if(StringUtil.isNoneBlank(destServerType)){
			templets = templetRepository.findByDestServerTypeAndSrcServerTypeIn(destServerType, serverTypes);
		}else {
			templets = templetRepository.findBySrcServerTypeIn(serverTypes);
		}

		List<String> destServerTypes = getDestServerTypes(templets);

		List<ServerEntrance> entranceList = entranceRepository.findByServerTypeIn(destServerTypes);
		// key:serverType_fc
		Map<String, List<ServerEntrance>> entranceMap = buildEntranceMap(entranceList);

		Map<String, String> serverPathMap = new HashMap<>();
		List<ServerRelation> saveList = new ArrayList<>();

		for(RelationTemplet t : templets){
			List<ServerInfo> srcInfoList = serverTypeMap.get(t.getSrcServerType());

			if(srcInfoList == null){
				continue;
			}

			for(ServerInfo src : srcInfoList){
				// ?????????????????????????????????????????????
				String srcServerId = src.getId();
				if(configIdMap.get(srcServerId) == null || !serverOnline(src)){
					continue;
				}

				buildRelation(t, entranceMap, serverIdMap, srcServerId, relationKeySet,
						saveList, serverPathMap);
			}
		}
		if(!saveList.isEmpty()){
			relationsRepsotory.save(saveList);
		}
		return this.editRelationNode(serverPathMap);
	}

	@Override
	public boolean imposedConfingRelation(List<String> serverTypes, List<String> serverIds) {
		List<ServerInfo> serverInfos = serverInfoRepsotory.findAll();
		Pair<Map<String, List<ServerInfo>>, Map<String, ServerInfo>> serverInfoPair = getServerTypeAndServerIdMap(serverInfos);
		// key:serverType  ?????????????????????????????????????????????
		Map<String, List<ServerInfo>> serverTypeMap = serverInfoPair.getLeft();
		// key:serverId  
		Map<String, ServerInfo> serverIdMap = serverInfoPair.getRight();

		Map<String, String> configIdMap = new HashMap<>();
		for(String s : serverIds){
			configIdMap.put(s, s);
		}

		List<RelationTemplet> templets = templetRepository.findBySrcServerTypeIn(serverTypes);
		List<String> destServerTypes = getDestServerTypes(templets);

		List<ServerEntrance> entranceList = entranceRepository.findByServerTypeIn(destServerTypes);
		// key:serverType_fc  
		Map<String, List<ServerEntrance>> entranceMap = buildEntranceMap(entranceList);

		Map<String, String> serverPathMap = new HashMap<>();
		List<ServerRelation> saveList = new ArrayList<>();

		Set<String> relationKeySet = new HashSet<>();
		for(RelationTemplet t : templets){
			List<ServerInfo> srcInfoList = serverTypeMap.get(t.getSrcServerType());

			if(srcInfoList == null){
				continue;
			}

			// ???????????????????????????
			for(ServerInfo src : srcInfoList){
				String srcServerId = src.getId();
				// ????????????????????????
				if(configIdMap.get(srcServerId) == null){
					continue;
				}

				buildRelation(t, entranceMap, serverIdMap, srcServerId, relationKeySet,
						saveList, serverPathMap);
			}
		}
		// ?????????????????????????????????
		relationsRepsotory.deleteBySrcServerIdIn(serverIds);
		if(!saveList.isEmpty()){
			relationsRepsotory.save(saveList);
		}

		return this.editRelationNode(serverPathMap);
	}

	/**
	 * ??????????????????  key:serverType_fc
	 * @param entranceList
	 * @return
	 */
	public Map<String, List<ServerEntrance>> buildEntranceMap(List<ServerEntrance> entranceList){
		Map<String, List<ServerEntrance>> entranceMap = new HashMap<>();
		for(ServerEntrance e : entranceList){
			String fcsStr = e.getFcs();
			if(StringUtils.isNotBlank(fcsStr)){
				List<String> fcs = JSON.parseArray(fcsStr, String.class);
				for(String fc : fcs){
					String key = e.getServerType()+"_"+fc.trim();
					List<ServerEntrance> list = entranceMap.get(key);
					if(list == null){
						list = new ArrayList<>();
					}
					list.add(e);
					entranceMap.put(key, list);
				}
			}
		}

		return entranceMap;
	}

	/**
	 * ??????????????????
	 * @param t ????????????
	 * @param destServer
	 * @param entrance
	 * @param srcServerId
	 * @return
	 */
	public ServerRelation buildRelation(RelationTemplet t, ServerInfo destServer,ServerEntrance entrance, String srcServerId){
		ServerRelation r = new ServerRelation();
		if(StringUtils.isNotBlank(t.getDescription())){
			r.setRelationName(t.getDescription());
		}else{
			r.setRelationName(t.getSrcServerType()+"_"+t.getDestProviderFc());
		}
		if(StringUtils.isBlank(destServer.getServerIp())){
			destServer.setServerIp(getServerIp(destServer));
		}
		r.setDestIp(destServer.getServerIp());
		r.setDestServerId(destServer.getId());
		r.setDestOnlineStatus(destServer.getOnlineStatus());

		r.setDestPort(entrance.getPort());
		r.setDestProtocol(entrance.getProtocol());
		r.setDestUrl(entrance.getURL());
		r.setPassword(entrance.getPassword());
		r.setUserName(entrance.getUserName());
		r.setDestEntranceId(entrance.getId());

		r.setDestProviderFc(t.getDestProviderFc());
		r.setDestServerType(t.getDestServerType());
		r.setSrcConsumerFc(t.getSrcConsumerFc());
		r.setSrcServerId(srcServerId);
		r.setSrcServerType(t.getSrcServerType());

		// ??????????????????id?????????????????????????????????????????????
		// ??????srcServerId_destServerId_destProviderFc???hashcode
//		String relationId = ServerRelationUtil.getRelationId(srcServerId, t.getSrcConsumerFc(), destServer.getId(), t.getDestProviderFc())
//		r.setId(relationId)
		r.setId(UUIDTOOL.getuuid(32));

		return r;
	}

	@Override
	public void validateRelation() {
		Map<String, String> pathMap = new HashMap<>();
		Map<String, String> ipMap = new HashMap<>();
		List<ServerInfo> infos = serverInfoRepsotory.findAll();
		for(ServerInfo info : infos){
			ipMap.put(info.getId(), info.getServerIp());
			String path = getRelationPath(info.getServerType(), info.getId());
			pathMap.put(info.getId(), path);
		}

		Map<String, Integer> portMap = new HashMap<>();
		List<ServerEntrance> entrances = entranceRepository.findAll();
		for(ServerEntrance entrance : entrances){
			String serverId = entrance.getServerId();
			String fcsStr = entrance.getFcs();
			if(StringUtils.isNotBlank(fcsStr)){
				List<String> fcs = JSON.parseArray(fcsStr, String.class);
				for(String fc : fcs){
					portMap.put(serverId+"_"+fc, entrance.getPort());
				}
			}
		}

		validateRelationNode(pathMap, ipMap, portMap);

		functionCodesService.validateFunctionCodes();
	}

	@Override
	public Map<String, Object> getRelationPage(Integer pageNumber,
			Integer pageSize, ServerRelation relation) {
		StringBuilder srcServerIds = new StringBuilder();
		StringBuilder destServerIds = new StringBuilder();

		String shql = " FROM ServerRelation ";
		if(StringUtils.isNoneBlank(relation.getSrcServerName())){
			shql += " AND serverName LIKE '%" + relation.getSrcServerName() + "%'";
		}
		if(StringUtils.isNoneBlank(relation.getSrcIp())){
			shql += " AND serverIp LIKE '%" + relation.getSrcIp() + "%'";
		}
		if(shql.indexOf("AND") > -1){
			shql = shql.replaceFirst("AND", "WHERE");
			List<ServerInfo> infos = serverInfoRepsotory.findByHQL(shql);
			for(ServerInfo i : infos){
				srcServerIds.append(",'").append(i.getId()).append("'");
			}
		}

		String dhql = " FROM ServerRelation ";
		if(StringUtils.isNoneBlank(relation.getDestServerName())){
			dhql += " AND serverName LIKE '%" + relation.getDestServerName() + "%'";
		}

		if(dhql.indexOf("AND") > -1){
			dhql = dhql.replaceFirst("AND", "WHERE");
			List<ServerInfo> infos = serverInfoRepsotory.findByHQL(dhql);
			for(ServerInfo i : infos){
				destServerIds.append(",'").append(i.getId()).append("'");
			}
		}

		PageInfo pageInfo = new PageInfo();
		pageInfo.setPage(pageNumber);
		pageInfo.setRows(pageSize);

		List<ServerRelation> pageRelation = relationsRepsotory.findPageByHQL(
				getRelationSearchHQL(relation, srcServerIds.toString()
						, destServerIds.toString()), pageInfo);

		List<ServerInfo> infoList = serverInfoRepsotory.findAll();
		Map<String, String> infoMap = new HashMap<>();
		for(ServerInfo s : infoList){
			infoMap.put(s.getId(), s.getServerName());
		}
		for(ServerRelation s : pageRelation){
			s.setSrcServerName(infoMap.get(s.getSrcServerId()));
			s.setDestServerName(infoMap.get(s.getDestServerId()));
		}

		Map<String, Object> result = new HashMap<>();
		result.put(ResultKeyConst.ROWS_KEY, pageRelation);
		result.put(ResultKeyConst.TOTAL_KEY, Math.ceil((double)pageInfo.getTotal()/(double)pageInfo.getPageSize()));
		result.put(ResultKeyConst.RECORDS_KEY, pageInfo.getTotal());

		return result;
	}

	private String getRelationSearchHQL(ServerRelation relation, String srcServerIds, String destServerIds){
		String hql = " FROM ServerRelation WHERE 1=1 ";
		if(StringUtils.isNoneBlank(relation.getRelationName())){
			hql += " AND relationName like '%" + relation.getRelationName() + "%'";
		}
		if(StringUtils.isNoneBlank(relation.getDestProtocol())){
			hql += " AND destProtocol like '%" + relation.getDestProtocol() + "%'";
		}
		if(StringUtils.isNoneBlank(relation.getSrcServerType())){
			hql += " AND srcServerType='" + relation.getSrcServerType() + "'";
		}
		if(StringUtils.isNoneBlank(relation.getDestServerType())){
			hql += " AND destServerType='" + relation.getDestServerType()+ "'";
		}
		if(StringUtils.isNoneBlank(relation.getDestIp())){
			hql += " AND destIp like '%" + relation.getDestIp() + "%'";
		}
		if(StringUtils.isNoneBlank(srcServerIds)){
			hql += " AND srcServerId IN (" + srcServerIds.replaceFirst(",", "") + ")";
		}
		if(StringUtils.isNoneBlank(destServerIds)){
			hql += " AND destServerId IN (" + destServerIds.replaceFirst(",", "") + ")";
		}
		if(StringUtils.isNotBlank(relation.getSrcServerId())){
			hql += " AND srcServerId ='" + relation.getSrcServerId() + "'";
		}
		if(StringUtils.isNoneBlank(relation.getDestServerId())){
			hql += " AND destServerId ='" + relation.getDestServerId() + "'";
		}
		if(StringUtils.isNoneBlank(relation.getSrcConsumerFc())){
			hql += " AND src_consumer_fc like '%" + relation.getSrcConsumerFc() + "%'";
		}
		if(StringUtils.isNoneBlank(relation.getDestProviderFc())){
			hql += " AND dest_provider_fc like '%" + relation.getDestProviderFc() + "%'";
		}
		hql += " order by srcServerType DESC, id DESC";
		return hql;
	}

	@Override
	public boolean editRelationNode(String serverType, String serverId) {
		try{
			CuratorFramework client = ZookeeperUtil.getCuratorClient().getClient();
			String path = getRelationPath(serverType, serverId);
			List<ServerRelation> list = relationsRepsotory.findBySrcServerId(serverId);
			String jsonData = JSON.toJSONString(list);
			if(client.checkExists().forPath(path) == null){
				client.create().creatingParentsIfNeeded().forPath(path);
			}
			client.setData().forPath(path, jsonData.getBytes(CommonConst.UTF8_CHARSET));
			return true;
		}catch(Exception e){
			logger.error("????????????????????????", e);
		}
		return false;
	}

	@Override
	public boolean editRelationNode(Map<String, String> serverPathMap) {
		try{
			CuratorFramework client = ZookeeperUtil.getCuratorClient().getClient();
			// ??????serverId???????????????????????????????????????,???????????????????????????
			List<ServerRelation> updateList = getRelationByServerIds(new ArrayList<>(serverPathMap.keySet()));
			// srcServerId-List<ServerRelation>
			Map<String, List<ServerRelation>> updateMap = new HashMap<>();
			for(ServerRelation r : updateList){
				if(updateMap.get(r.getSrcServerId()) == null){
					List<ServerRelation> l = new ArrayList<>();
					updateMap.put(r.getSrcServerId(), l);
				}
				updateMap.get(r.getSrcServerId()).add(r);
			}

			for(Entry<String, String> entry : serverPathMap.entrySet()){
				List<ServerRelation> l = updateMap.get(entry.getKey());
				String data = "";
				if(l != null && !l.isEmpty()){
					data = JSON.toJSONString(l);
				}
				String path = entry.getValue();
				if(client.checkExists().forPath(path) == null){
					client.create().creatingParentsIfNeeded().forPath(path);
				}
				logger.info("editRelationNode???????????????{}",path);
				client.setData().forPath(path, data.getBytes(CommonConst.UTF8_CHARSET));
			}
			return true;
		}catch(Exception e){
			logger.error("editRelationNode?????????serverPathMap:{}"+serverPathMap);
		}
		return false;
	}

	@Override
	public Map<String, Object> getServerRelationTree(Integer pageNumber,
			Integer pageSize, ServerRelation relation) {
		Map<String, Object> result = new HashMap<>();
		List<RelationTreeModel> tree = new ArrayList<>();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPage(pageNumber);
		pageInfo.setRows(pageSize);
		String hql = " FROM ServerRelation WHERE 1=1 ";
		if(StringUtils.isNoneBlank(relation.getRelationName())){
			hql += " AND relationName like '%" + relation.getRelationName() + "%'";
		}
		hql += "order by srcServerType DESC, srcServerId DESC";
		List<ServerRelation> relations = relationsRepsotory.findPageByHQL(hql, pageInfo);
		Map<String, RelationTreeModel> map = new HashMap<>();

		List<ServerInfo> infoList = serverInfoRepsotory.findAll();
		Map<String, String> infoMap = new HashMap<>();
		for(ServerInfo s : infoList){
			infoMap.put(s.getId(), s.getServerName());
		}

		for(ServerRelation r : relations){
			RelationTreeModel model = new RelationTreeModel(r);
			String srcServerId = r.getSrcServerId();
			String srcServerType = r.getSrcServerType();

			RelationTreeModel treemodel = map.get(srcServerType);
			if(treemodel == null){
				String srcServerName = infoMap.get(model.getSrcServerId());
				treemodel = new RelationTreeModel();
				treemodel.setId(srcServerType);
				treemodel.setRelationName(srcServerName);
				tree.add(treemodel);
				map.put(srcServerType, treemodel);
			}

			RelationTreeModel pmodel = map.get(srcServerId);
			if(map.get(srcServerId) == null){
				pmodel = new RelationTreeModel();
				pmodel.setId(srcServerId);
				pmodel.setRelationName(srcServerId);
				pmodel.setPid(srcServerType);

				treemodel.getChildren().add(pmodel);
				map.put(srcServerId, pmodel);
			}
			model.setDestServerName(infoMap.get(model.getDestServerId()));
			pmodel.getChildren().add(model);
		}
		result.put(ResultKeyConst.ROWS_KEY, tree);
		result.put(ResultKeyConst.TOTAL_KEY, pageInfo.getTotal());
		return result;
	}

	@Override
	public boolean updateRelation(String serverId,
			List<ServerRelation> relationList) {
		relationsRepsotory.deleteBySrcServerId(serverId);
		return batchSave(relationList);
	}

	@Override
	public boolean deleteRelation(String id) {
		ServerRelation relation = relationsRepsotory.findOne(id);
		if(relation != null){
			String serverId = relation.getSrcServerId();
			String serverType = relation.getSrcServerType();
			relationsRepsotory.delete(relation);
			return editRelationNode(serverType, serverId);
		}
		return false;
	}

	@Override
	public boolean deleteRelationByServerId(String serverId) {
		List<ServerRelation> relations = relationsRepsotory.findBySrcServerId(serverId);
		if(relations != null && !relations.isEmpty()){
			String serverType = relations.get(0).getSrcServerType();
			relationsRepsotory.delete(relations);
			return editRelationNode(serverType, serverId);
		}
		return false;
	}

	@Override
	public boolean deleteRelationByDestServerId(String serverId) {
		List<ServerRelation> relations = relationsRepsotory.findByDestServerId(serverId);
		if(relations != null && !relations.isEmpty()){
			Map<String, String> pathMap = new HashMap<>();
			for(ServerRelation r : relations){
				String srcServerId = r.getSrcServerId();
				pathMap.put(srcServerId, getRelationPath(r.getSrcServerType(), srcServerId));
			}
			relationsRepsotory.delete(relations);
			return this.editRelationNode(pathMap);
		}
		return false;
	}

	@Override
	public void exportRelation(HttpServletResponse response) {
		String [] title = {"?????????","??????????????????","????????????","????????????ID","???????????????","??????????????????","???????????????","???????????????ID","???????????????????????????","????????????IP??????","????????????????????????","??????????????????????????????","????????????????????????","????????????"};


		List<ServerRelation> pageRelation = relationsRepsotory.findAll();

		List<ServerInfo> infoList = serverInfoRepsotory.findAll();
		Map<String, String> infoMap = new HashMap<>();
		for(ServerInfo s : infoList){
			infoMap.put(s.getId(), s.getServerName());
		}
		for(ServerRelation s : pageRelation){
			s.setSrcServerName(infoMap.get(s.getSrcServerId()));
			s.setDestServerName(infoMap.get(s.getDestServerId()));
		}
		createSheet(response,pageRelation,title);
	}

	public void createSheet(HttpServletResponse response,List<ServerRelation> serverRelationList,String[] titles) {
		OutputStream out = null;
		try{
            //1.???????????????
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle colStyle = createCellStyle(workbook,(short)13);
            //2.???????????????
            HSSFSheet sheet = workbook.createSheet("????????????");
            //2.1???????????????????????????
            //??????????????????
            sheet.setDefaultColumnWidth(25);

            //3.2???????????????;?????????????????????
            HSSFRow row2 = sheet.createRow(0);

            for(int i=0;i<titles.length;i++)
            {
                HSSFCell cell2 = row2.createCell(i);
                //?????????????????????
                cell2.setCellStyle(colStyle);
                cell2.setCellValue(titles[i]);
            }

            if(serverRelationList != null && !serverRelationList.isEmpty()){
            	for (int i = 0; i < serverRelationList.size(); i++) {
					HSSFRow row3 = sheet.createRow(i+1);
					HSSFCell cell1 = row3.createCell(0);
					cell1.setCellValue(serverRelationList.get(i).getRelationName());
					HSSFCell cell2 = row3.createCell(1);
					cell2.setCellValue(serverRelationList.get(i).getSrcServerType());
					HSSFCell cell3 = row3.createCell(2);
					cell3.setCellValue(serverRelationList.get(i).getSrcServerName());
					HSSFCell cell4 = row3.createCell(3);
					cell4.setCellValue(serverRelationList.get(i).getSrcServerId());
					HSSFCell cell5 = row3.createCell(4);
					cell5.setCellValue(serverRelationList.get(i).getSrcConsumerFc());
					HSSFCell cell6 = row3.createCell(5);
					cell6.setCellValue(serverRelationList.get(i).getDestServerType());
					HSSFCell cell7 = row3.createCell(6);
					cell7.setCellValue(serverRelationList.get(i).getDestServerName());
					HSSFCell cell8 = row3.createCell(7);
					cell8.setCellValue(serverRelationList.get(i).getDestServerId());
			 		HSSFCell cell9 = row3.createCell(8);
					cell9.setCellValue(serverRelationList.get(i).getDestProviderFc());
					HSSFCell cell10 = row3.createCell(9);
					cell10.setCellValue(serverRelationList.get(i).getDestIp());
					HSSFCell cell11 = row3.createCell(10);
					cell11.setCellValue(serverRelationList.get(i).getDestPort());
					HSSFCell cell12 = row3.createCell(11);
					cell12.setCellValue(serverRelationList.get(i).getDestProtocol());
					HSSFCell cell13 = row3.createCell(12);
					cell13.setCellValue(serverRelationList.get(i).getDestUrl());
				}
            }

            StringBuilder sb=new StringBuilder();
			String str = Pattern.compile("\\.xlsx$", Pattern.CASE_INSENSITIVE).matcher("??????????????????.xlsx").replaceAll("");//??????.xls??????
			str = java.net.URLEncoder.encode(str, "UTF-8");//???????????????????????????
			sb.append(str);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.addHeader("Content-Disposition","attachment;charset=UTF-8;filename="+sb.toString()+".xls");
			out = response.getOutputStream();
			workbook.write(out);
            workbook.close();
        }catch(Exception e)
        {
            logger.error("????????????excel??????", e);
        }
	}

	@SuppressWarnings("all")
	private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize) {
	    HSSFCellStyle style = workbook.createCellStyle();
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//????????????
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//????????????
	    //????????????
	    HSSFFont font = workbook.createFont();
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    font.setFontHeightInPoints(fontsize);
	    //????????????
	    style.setFont(font);
	    return style;
	}

	@Override
	public Map<String, Object> getRelationMap(List<String> serverIds) {
		return new HashMap<>();
	}

	public void addLinkData(String id, String source, String target, String color, List<MapDataModel> linkDatas){
		LinkDataModel linkData = new LinkDataModel();
		linkData.setId(id);
		linkData.setSource(source);
		linkData.setTarget(target);
		linkData.setColor(StringUtils.isBlank(color)?"#d9dcde":color);
		MapDataModel link = new MapDataModel(linkData);
		linkDatas.add(link);
	}

	public void addNodeData(String id, String name,String parent,String type, List<MapDataModel> mapDatas){
		NodeDataModel idNode = new NodeDataModel();
		idNode.setId(id);
		idNode.setName(name);
		if(StringUtils.isNotBlank(parent)){
			idNode.setParent(parent);
		}
		if(StringUtils.isNotBlank(type)){
			idNode.setType(type);
		}
		MapDataModel idData = new MapDataModel(idNode);
		mapDatas.add(idData);
	}

	@Override
	public Map<String, Object> deleteRelationByLine(String lineId) {
		Map<String, Object> result = new HashMap<>();
		try{
			String srcNode = lineId.split(split2)[0];
			String destNode = lineId.split(split2)[1];
			String[] srcInfo = srcNode.split(split1);
			String[] destInfo = destNode.split(split1);
			String hql = " DELETE FROM ServerRelation WHERE srcServerType='" + srcInfo[0]
					+ "' AND srcServerId='" + srcInfo[1] + "' AND srcConsumerFc='" + srcInfo[2]
					+ "' AND destServerType='" + destInfo[0] + "' AND destServerId='" + destInfo[1]
					+ "' AND destProviderFc='" + destInfo[2] + "'";
			relationsRepsotory.executeHQL(hql);
			result.put(ResultKeyConst.SUCESS_KEY, editRelationNode(srcInfo[0], srcInfo[1]));
		}catch(Exception e){
			logger.error("???????????????", e);
			result.put(ResultKeyConst.SUCESS_KEY, false);
		}

		return result;
	}

	@Override
	public Map<String, Object> addRelationLine(String srcNode, String destNode,
			String serverIds) {
		Map<String, Object> result = new HashMap<>();
		String[] srcInfo = srcNode.split(split1);
		String[] destInfo = destNode.split(split1);
		String destServerId = destInfo[1];
		String destProviderFc = destInfo[2];
		String ehql = " FROM ServerEntrance WHERE serverId='" + destServerId
				+ "' AND fcs like '%" + destProviderFc + "%'";
		List<ServerEntrance> eList = entranceRepository.findByHQL(ehql);
		if(eList.size() == 1){
			ServerEntrance entrance = eList.get(0);
			ServerInfo destServer = serverInfoRepsotory.findOne(destServerId);
			ServerRelation relation = new ServerRelation(entrance);
			relation.setSrcServerType(srcInfo[0]);
			relation.setSrcServerId(srcInfo[1]);
			relation.setSrcConsumerFc(srcInfo[2]);
			relation.setDestProviderFc(destProviderFc);
			relation.setDestIp(destServer.getServerIp());
			List<String> serverIdList = JSON.parseArray(serverIds, String.class);
			result.put(ResultKeyConst.SUCESS_KEY, true);
			result.put(ResultKeyConst.ROWS_KEY, serverIdList);
		}else{
			result.put(ResultKeyConst.SUCESS_KEY, false);
			result.put("msg", "???????????????????????????");
		}
		return result;
	}

	@Override
	public void configRelation(String destServerType) {
		List<RelationTemplet> templets = templetRepository.findByDestServerType(destServerType);
		Set<String> serverTypeSet = new TreeSet<>();
		for(RelationTemplet t : templets){
			serverTypeSet.add(t.getSrcServerType());
		}
		if(!serverTypeSet.isEmpty()){
			List<String> serverTypes = new ArrayList<>(serverTypeSet);
			List<ServerInfo> serverInfos = serverInfoRepsotory.findByServerTypeIn(serverTypes);
			if(serverInfos != null && !serverInfos.isEmpty()){
				List<String> serverIds = new ArrayList<>();
				for(ServerInfo i : serverInfos){
					serverIds.add(i.getId());
				}
				this.configRelationByTemple(serverTypes, serverIds, destServerType);
			}
		}
	}

	@Override
	public Map<String, Object> deleteRelationByIds(String ids) {
		Map<String, Object> resule = new HashMap<>();
		Map<String, String> pathMap = new HashMap<>();
		List<String> idList = JSON.parseArray(ids, String.class);
		if(idList != null && !idList.isEmpty()){
			List<ServerRelation> relations = relationsRepsotory.findByIdIn(idList);
			for(ServerRelation r : relations){
				String srcServerId = r.getSrcServerId();
				pathMap.put(srcServerId, getRelationPath(r.getSrcServerType(), srcServerId));
			}
			relationsRepsotory.delete(relations);
			resule.put("success", this.editRelationNode(pathMap));
		}
		return resule;
	}

	@Override
	public void deleteBySrcServerId(String srcServerId) {
		try{
			relationsRepsotory.deleteBySrcServerId(srcServerId);
		}catch(Exception e){
			logger.error("?????????????????????srcServerId" + srcServerId, e);
		}
	}

	@Override
	public void serverOnOffLine(String destServerId, Integer onlineStatu) {
		List<ServerRelation> relations = relationsRepsotory.findByDestServerId(destServerId);
		if(relations != null && !relations.isEmpty()){
			// ?????????????????????????????????????????????   key:serverId  value:path
			Map<String, String> srcServerMap = new HashMap<>();
			for(ServerRelation r : relations){
				r.setDestOnlineStatus(onlineStatu);
				String srcServerId = r.getSrcServerId();
				if(srcServerMap.get(srcServerId) == null){
					srcServerMap.put(srcServerId, getRelationPath(r));
				}
			}
			relationsRepsotory.save(relations);
			editRelationNode(srcServerMap);
		}

		functionCodesService.updateFunctionCodes(destServerId);
	}

	/**
	 * ??????????????????????????????????????????
	 * @param r
	 * @return
	 */
	protected String getRelationPath(ServerRelation r){
		return getRelationPath(r.getSrcServerType(), r.getSrcServerId());
	}

	private String getRelationPath(String serverType, String serverId){
		return ZookeeperUtil.getRelationPath(serverType, serverId);
	}

	/**
	 * ??????????????????????????????????????????
	 * @param templets
	 * @return
	 */
	private List<String> getDestServerTypes(List<RelationTemplet> templets){
		// ???????????????????????????
		List<String> destServerTypes = new ArrayList<>();
		for(RelationTemplet t : templets){
			if(StringUtils.isNotBlank(t.getDestServerType()) && !destServerTypes.contains(t.getDestServerType())){
				destServerTypes.add(t.getDestServerType());
			}
		}

		return destServerTypes;
	}

	/**
	 *
	 * @param serverInfos
	 * @return left:serverType-List<ServerInfo>???right:serverId-ServerInfo
	 */
	private Pair<Map<String, List<ServerInfo>>, Map<String, ServerInfo>> getServerTypeAndServerIdMap(List<ServerInfo> serverInfos){
		// key:serverType  ?????????????????????????????????????????????
		Map<String, List<ServerInfo>> serverTypeMap = new HashMap<>();
		// key:serverId  
		Map<String, ServerInfo> serverIdMap = new HashMap<>();
		for(ServerInfo i : serverInfos){
			serverIdMap.put(i.getId(), i);
			List<ServerInfo> list = serverTypeMap.get(i.getServerType());
			if(list == null){
				list = new ArrayList<>();
				serverTypeMap.put(i.getServerType(), list);
			}
			list.add(i);
		}

		return Pair.of(serverTypeMap, serverIdMap);
	}

	/**
	 *
	 * @param templet ????????????
	 * @param entranceMap ???????????? key:destServerType_destProviderFc
	 * @param serverIdMap ?????????????????????key:serverId
	 * @param srcServerId ???????????????id
	 * @param relationKeySet ??????????????????key?????? key:srcServerId_srcConsumerFc_destServerId_destProviderFc
	 * @param saveList ?????????????????????????????????
	 * @param serverPathMap ?????????????????????key???srcServerId
	 */
	private void buildRelation(RelationTemplet templet, Map<String, List<ServerEntrance>> entranceMap
			, Map<String, ServerInfo> serverIdMap, String srcServerId, Set<String> relationKeySet,
			List<ServerRelation> saveList, Map<String, String> serverPathMap){

		String entranceKey = templet.getDestServerType()+"_"+templet.getDestProviderFc().trim();
		// ??????????????????????????????????????????????????????
		List<ServerEntrance> entrances = entranceMap.get(entranceKey);

		if(entrances != null){
			for(ServerEntrance entrance : entrances){
				ServerInfo destServer = serverIdMap.get(entrance.getServerId());
				if(destServer == null){
					logger.info("??????????????????????????????ServerId:{}",entrance.getServerId());
					continue;
				}
				String relationKey = srcServerId+"_"+templet.getSrcConsumerFc()+"_"
				+entrance.getServerId()+"_"+templet.getDestProviderFc();
				// ???????????????????????????????????????
				if(relationKeySet.add(relationKey)){
					ServerRelation r = buildRelation(templet, destServer, entrance, srcServerId);
					saveList.add(r);

					if(serverPathMap.get(srcServerId) == null){
						serverPathMap.put(srcServerId, getRelationPath(r.getSrcServerType(), srcServerId));
					}
				}
			}
		}else{
			logger.info("????????????????????????????????????entranceKey:{}",entranceKey);
		}
	}

	/**
	 *
	 * @param pathMap
	 * @param ipMap
	 * @param portMap
	 */
	private void validateRelationNode(Map<String, String> pathMap, Map<String, String> ipMap, Map<String, Integer> portMap){
		try{
			CuratorFramework client = ZookeeperUtil.getCuratorClient().getClient();
			Collection<String> paths = pathMap.values();
			for(String path : paths){
				String data = new String(client.getData().forPath(path));

				if(StringUtils.isBlank(data)){
					continue;
				}

				List<ServerRelation> updateRelations = new ArrayList<>();
				List<ServerRelation> relations = JSON.parseArray(data, ServerRelation.class);
				for(ServerRelation r : relations){
					String serverId = r.getDestServerId();
					String destIp = ipMap.get(serverId);
					String portKey = r.getDestServerId()+"_"+r.getDestProviderFc();
					Integer port = portMap.get(portKey);
					if((destIp != null && !destIp.equals(r.getDestIp()))
							|| (port != null && !port.equals(r.getDestPort()))){
						r.setDestIp(destIp);
						r.setDestPort(port);
						updateRelations.add(r);
					}
				}
				if(!updateRelations.isEmpty()){
					client.setData().forPath(path, JSON.toJSONString(relations).getBytes(CommonConst.UTF8_CHARSET));
					relationsRepsotory.save(updateRelations);
				}
			}
		}catch(Exception e){
			logger.error("??????????????????", e);
		}
	}

	@Override
	public ResultBean<String> generateRelationByServerType(List<String> destServerType) {
		List<ServerInfo> destServerInfoList = serverInfoRepsotory.findByServerTypeIn(destServerType);
		List<String> serverIds = new ArrayList<>();
		destServerInfoList.forEach(serverInfo -> serverIds.add(serverInfo.getId()));
		try {
			ResultBean<String> resultBean = new ResultBean<>();
			boolean result = configRelationByTemple(destServerType, serverIds);
			if(result) {
				resultBean.setCode(0);
				resultBean.setMessage("??????????????????");
			}else {
				logger.warn("????????????????????????????????????{}", JSON.toJSONString(destServerType));
				resultBean.setCode(-1);
				resultBean.setMessage("??????????????????");
			}

			return resultBean;
		}catch(Exception e) {
			logger.warn("????????????????????????????????????{}", JSON.toJSONString(destServerType));
			ResultBean<String> resultBean = new ResultBean<>();
			resultBean.setCode(-1);
			resultBean.setMessage("?????????????????????" + e.getMessage());
			return resultBean;
		}
	}

}

