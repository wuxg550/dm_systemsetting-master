package com.hy.zookeeper.config.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hy.zookeeper.config.base.BaseDaoRepository;
import com.hy.zookeeper.config.entity.AgreementType;

public interface AgreementTypeRepository extends BaseDaoRepository<AgreementType, String>, JpaSpecificationExecutor<AgreementType>{


}
