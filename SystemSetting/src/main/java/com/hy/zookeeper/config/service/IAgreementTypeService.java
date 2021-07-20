package com.hy.zookeeper.config.service;

import java.util.List;
import java.util.Map;

import com.hy.zookeeper.config.entity.AgreementType;


public interface IAgreementTypeService {
	
	/**
	 * 获取所有的协议类型
	 * @return
	 */
     List<AgreementType> getAll();
     
     /**
      * 保存协议类型
      * @param agreementType 实体
      * @return
      */
     boolean save(AgreementType agreementType);
     
     /**
      * 根据id查看协议类型
      * @param id 
      * @return
      */
     AgreementType getById(String id);
     
     
     /**
      * 分页获取协议类型
      * @param page 当前页
      * @param row 一页总数
      * @return
      */
     Map<String,Object> findAllPage(int page, int row);
     
     
     boolean deleteById(String id);

}
