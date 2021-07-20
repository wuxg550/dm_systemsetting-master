package com.hy.zookeeper.config.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hy.zookeeper.config.common.PageInfo;
import com.hy.zookeeper.config.dao.AgreementTypeRepository;
import com.hy.zookeeper.config.entity.AgreementType;
import com.hy.zookeeper.config.service.IAgreementTypeService;
import com.hy.zookeeper.config.util.UUIDTOOL;

@Service
public class AgreementTypeServiceImpl implements IAgreementTypeService {

	@Autowired
	private AgreementTypeRepository agreementTypeRepository;

	@Override
	public List<AgreementType> getAll() {

		return agreementTypeRepository.findAll();
	}

	@Override
	public boolean save(AgreementType agreementType) {
		if("".equals(agreementType.getId())){
			agreementType.setId(UUIDTOOL.getuuid(32));
		}
		AgreementType newAgreementType = agreementTypeRepository.save(agreementType);

		return newAgreementType!=null;
	}

	@Override
	public AgreementType getById(String id) {

		return agreementTypeRepository.findOne(id);
	}

	@Override
	public Map<String, Object> findAllPage(int page, int row) {
		Map<String,Object> result = new HashMap<>();
		//serverTypeRepsotory.
		PageInfo pageInfo = new PageInfo();
		String sql = "SELECT * FROM BASISDATA1.PLATFORM_AGREEMENT_TYPE";
		pageInfo.setPage(page);
		pageInfo.setRows(row);
		List<AgreementType> agreementTypeList = agreementTypeRepository.findPageBySQL(sql, pageInfo);
		result.put("rows", agreementTypeList);
		int total = (int) (pageInfo.getTotal()%row==0?pageInfo.getTotal()/row:pageInfo.getTotal()/row+1);
		result.put("total", total);
		result.put("records", pageInfo.getTotal());
		return result;
	}

	@Override
	public boolean deleteById(String id) {
		agreementTypeRepository.delete(id);
		return true;
	}

}
