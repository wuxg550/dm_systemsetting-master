package com.hy.zookeeper.config.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hy.zookeeper.config.base.BaseDaoRepository;
import com.hy.zookeeper.config.entity.ServerEntrance;

public interface ServerEntranceRepository extends BaseDaoRepository<ServerEntrance, String>, JpaSpecificationExecutor<ServerEntrance>{

	public ServerEntrance findByServerIdAndPort(String serverId, Integer port);
	
	public Integer deleteByServerId(String serverId);
	
	public List<ServerEntrance> findByServerId(String serverId);
	
	public List<ServerEntrance> findByServerIdAndServerType(String serverId,String serverType);
	
	public List<ServerEntrance> findByServerTypeIn(List<String> serverTypes);
	
	public List<ServerEntrance> findByServerIdIn(List<String> serverIds);
	
	Integer deleteByServerTypeNotIn(List<String> serverTypes);
	
	Integer deleteByServerIdNotIn(List<String> serverIds);
	
	Integer deleteByServerIdIn(List<String> serverIds);
}
