package com.hy.zookeeper.config.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.common.PageInfo;
import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.dao.ServerTypeRepsotory;
import com.hy.zookeeper.config.entity.PlatformServerType;
import com.hy.zookeeper.config.service.IServerTypeService;
import com.hy.zookeeper.config.util.UUIDTOOL;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@Service
public class ServerTypeServiceImpl implements IServerTypeService{

	protected static final Logger logger = LoggerFactory.getLogger(ServerTypeServiceImpl.class);

	@Autowired
	private ServerTypeRepsotory serverTypeRepsotory;

	@Autowired
	private ServerInfoRepsotory serverInfoRepsotory;

	@Value("${root.region}")
	private String region;

	@Override
	public Map<String,Object> getAllTypePage(int page,int row, PlatformServerType serverType) {
		Map<String,Object> result = new HashMap<>();
		//serverTypeRepsotory.
		PageInfo pageInfo = new PageInfo();
		StringBuilder sql = new StringBuilder("SELECT * FROM PLATFORM_SERVER_TYPE");
		if(serverType != null){
			if(StringUtils.isNotBlank(serverType.getServerTypeName())){
				sql.append(" and server_type_name like '%").append(serverType.getServerTypeName()).append("%' ");
			}
			if(StringUtils.isNotBlank(serverType.getServerType())){
				sql.append(" and server_type like '%").append(serverType.getServerType()).append("%' ");
			}
		}
		pageInfo.setPage(page);
		pageInfo.setRows(row);
		List<PlatformServerType> serverTypeList = serverTypeRepsotory
				.findPageBySQL(sql.toString().replaceFirst("and", "where"), pageInfo);
		result.put("rows", serverTypeList);
		int total = (int) (pageInfo.getTotal()%row==0?pageInfo.getTotal()/row:pageInfo.getTotal()/row+1);
		result.put("total", total);
		result.put("records", pageInfo.getTotal());

		return result;
	}

	@Override
	public List<PlatformServerType> getNoUseType(String serverId) {
		return serverTypeRepsotory.findAll();
	}

	@Override
	public PlatformServerType getTypeByID(String id) {

		return serverTypeRepsotory.findOne(id);
	}

	@Override
	public boolean deleteType(String id) {
		try{
			serverTypeRepsotory.delete(id);
			setServiceTypeNodeData();
			return true;
		}catch(Exception e){
			logger.error("删除服务类型", e);
		}

		return false;
	}

	@Override
	public boolean saveServerType(PlatformServerType serverType) {

		try{
			if("".equals(serverType.getId())){
				serverType.setId(UUIDTOOL.getuuid(32));
			}

			serverTypeRepsotory.save(serverType);
			String path = "/S4"+ZookeeperUtil.SERVERS;
			String relation = "/S4"+ZookeeperUtil.RELATIONCONFIG;

			ZookeeperUtil.getCuratorClient().createIfPathNotExist(path+ZookeeperUtil.SEPARATOR+serverType.getServerType(), "");

			ZookeeperUtil.getCuratorClient().createIfPathNotExist(relation+ZookeeperUtil.SEPARATOR+serverType.getServerType(), "");

			ZookeeperUtil.getCuratorClient().createIfPathNotExist(path+ZookeeperUtil.SEPARATOR+serverType.getServerType()+ZookeeperUtil.CONSUMER, "");

			ZookeeperUtil.getCuratorClient().createIfPathNotExist(path+ZookeeperUtil.SEPARATOR+serverType.getServerType()+ZookeeperUtil.PROVIDER, "");

			ZookeeperUtil.getCuratorClient().createIfPathNotExist(path+ZookeeperUtil.SEPARATOR+serverType.getServerType()+ZookeeperUtil.INSTANCE, "");

			setServiceTypeNodeData();
			return true;
		}catch(Exception e){
			logger.error("保存服务类型", e);
		}
		return false;
	}

	@Override
	public List<PlatformServerType> getAllType() {

		return serverTypeRepsotory.findAll();
	}

	@Override
	public long getCount(PlatformServerType serverType) {
		StringBuilder sql = new StringBuilder(" SELECT count(*) FROM PLATFORM_SERVER_TYPE ");
		if(serverType != null){
			if(StringUtils.isNotBlank(serverType.getServerType())){
				sql.append(" and server_type = '").append(serverType.getServerType()).append("' ");
			}
			if(StringUtils.isNotBlank(serverType.getId())){
				sql.append(" and id <> '").append(serverType.getId()).append("' ");
			}
		}
		return serverTypeRepsotory.countBySQL(sql.toString().replaceFirst("and", "where"));
	}

	@Override
	public void setServiceTypeNodeData() {
		String serviceTypePath = ZookeeperUtil.SEPARATOR + region + ZookeeperUtil.SERVICETYPE;
		ZookeeperUtil.getCuratorClient().createOrUpdatePath(serviceTypePath, JSON.toJSONString(getAllType()));
	}

}
