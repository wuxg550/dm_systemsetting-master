package com.hy.zookeeper.config.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.hy.zookeeper.config.entity.RelationTemplet;

public interface IRelationTempletService {

	/**
	 * 根据SrcServerType集合获取所有模板
	 * @param srcServerTypes
	 * @return
	 */
	public List<RelationTemplet> getTempletBySrcServerTypes(List<String> srcServerTypes);
	
	public Map<String, Object> getTempletPage(Integer pageNumber, Integer pageSize, RelationTemplet templet);
	
	RelationTemplet getTempletById(String id);
	
	boolean saveTemplet(RelationTemplet templet);
	
	boolean deleteTemplet(String id);
	
	Map<String, Object> getALLTempletFc();
	
	boolean existTmeplet(RelationTemplet templet);
	
	void downloadTempletExcel(RelationTemplet templet, HttpServletResponse response);
	
	Map<String, Object> uploadExcelTemplet(List<MultipartFile> files);
}
