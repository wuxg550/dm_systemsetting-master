package com.hy.zookeeper.config.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.dao.ServerTypeRepsotory;
import com.hy.zookeeper.config.entity.PlatformServerType;
import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.service.IZkserverService;
import com.hy.zookeeper.config.systemconfig.StartConfigApplication;
import com.hy.zookeeper.config.util.ZookeeperUtil;
@Service
@Transactional(value="jpaTransactionManager")
public class ZkserverServiceImpl implements IZkserverService {

	protected static final Logger logger = LoggerFactory.getLogger(ZkserverServiceImpl.class);

	@Autowired
	private ServerTypeRepsotory serverTypeRepsotory;
	@Autowired
	private ServerInfoRepsotory serverInfoRepsotory;
	@Autowired
	private StartConfigApplication startConfig;

	@Value("${root.region}")
	private String region;

	@Value("${zookeeper.address}")
	private String zkConnectString;

	@Override
	public List<Zkserver> findByPage(Zkserver zkserver, int page, int rows) {
		return new ArrayList<>();
	}

	@Override
	public boolean saveServer(Zkserver zkserver) {
		return false;
	}

	@Override
	public Zkserver findZkserverById(String id) {
		return null;
	}

	@Override
	public boolean delServer(String id) {
		return false;
	}

	@Override
	public boolean initNode(String ip,String port){
		try{
			List<PlatformServerType> typeList = serverTypeRepsotory.findAll();
			//更新zookeeper服务 重新连接zookeeper
			if(ZookeeperUtil.getCuratorClient() != null){
				ZookeeperUtil.getCuratorClient().close();
			}
			String connectString = this.getConnectString();
			// 重连及注册监听事件
			startConfig.intiNode(connectString, typeList, region);
			return true;
		}catch(Exception e){
			logger.error("初始服务信息节点异常", e);
		}
		return false;
	}

	@Override
	public String getConnectString() {
		return zkConnectString.trim();
	}

}
