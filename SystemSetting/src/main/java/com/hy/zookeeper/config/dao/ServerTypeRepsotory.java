package com.hy.zookeeper.config.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hy.zookeeper.config.base.BaseDaoRepository;
import com.hy.zookeeper.config.entity.PlatformServerType;

public interface ServerTypeRepsotory extends BaseDaoRepository<PlatformServerType, String>,JpaSpecificationExecutor<PlatformServerType>{

}
