package com.hy.zookeeper.config.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hy.zookeeper.config.base.BaseDaoRepository;
import com.hy.zookeeper.config.entity.ServerInfo;

public interface ServerInfoRepsotory extends BaseDaoRepository<ServerInfo, String>,JpaSpecificationExecutor<ServerInfo> {

	public List<ServerInfo> findByServerTypeIn(List<String> serverTypes);
	
	Integer deleteByServerTypeNotIn(List<String> serverTypes);
	
	public List<ServerInfo> findByOnlineStatus(Integer onlineStatus);
	
	public List<ServerInfo> findByIdIn(List<String> serverIds);
	
	public List<ServerInfo> findByConfingFalg(String confingFalg);
	
	Integer deleteByIdNotIn(List<String> serverIds);
	
	public Integer deleteByConfingFalgNot(String confingFalg);
}
