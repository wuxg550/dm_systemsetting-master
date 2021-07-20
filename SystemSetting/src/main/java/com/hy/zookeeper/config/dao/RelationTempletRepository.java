package com.hy.zookeeper.config.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hy.zookeeper.config.base.BaseDaoRepository;
import com.hy.zookeeper.config.entity.RelationTemplet;

public interface RelationTempletRepository  extends BaseDaoRepository<RelationTemplet, String>, JpaSpecificationExecutor<RelationTemplet>{

	public List<RelationTemplet> findBySrcServerTypeIn(List<String> srcServerTypes);
	
	public List<RelationTemplet> findByDestServerType(String destServerType);
	
	public List<RelationTemplet> findByDestServerTypeAndSrcServerTypeIn(String destServerType, List<String> srcServerTypes);
}
