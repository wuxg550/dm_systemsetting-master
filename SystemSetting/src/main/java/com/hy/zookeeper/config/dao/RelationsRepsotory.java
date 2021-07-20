package com.hy.zookeeper.config.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hy.zookeeper.config.base.BaseDaoRepository;
import com.hy.zookeeper.config.entity.ServerRelation;

public interface RelationsRepsotory extends BaseDaoRepository<ServerRelation, String>, JpaSpecificationExecutor<ServerRelation>{

	public List<ServerRelation> findBySrcServerId(String srcServerId);
	
	public List<ServerRelation> findByDestServerId(String destServerId);
	
	public List<ServerRelation> findByDestServerIdAndDestPort(String destServerId, Integer destPort);
	
	public Integer deleteBySrcServerId(String srcServerId);
	
	public List<ServerRelation> findBySrcServerIdIn(List<String> srcServerIds);
	
	public List<ServerRelation> findBySrcServerTypeIn(List<String> srcServerTypes);
	
	public Integer deleteBySrcServerIdIn(List<String> srcServerIds);
	
	public Integer deleteByDestServerId(String destServerId);
	
	List<ServerRelation> findByIdIn(List<String> ids);
	
}