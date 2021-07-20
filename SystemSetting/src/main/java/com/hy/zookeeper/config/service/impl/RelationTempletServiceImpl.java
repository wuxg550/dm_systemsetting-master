package com.hy.zookeeper.config.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hy.zookeeper.config.common.PageInfo;
import com.hy.zookeeper.config.dao.RelationTempletRepository;
import com.hy.zookeeper.config.entity.RelationTemplet;
import com.hy.zookeeper.config.enums.ResultKeyConst;
import com.hy.zookeeper.config.service.IRelationTempletService;
import com.hy.zookeeper.config.util.ExportExcelUtil;
import com.hy.zookeeper.config.util.UUIDTOOL;

@Service("relationTempletService")
public class RelationTempletServiceImpl implements IRelationTempletService{

	private static final Logger logger = LoggerFactory.getLogger(RelationTempletServiceImpl.class);

	@Resource
	private RelationTempletRepository templetRepository;

	@Override
	public List<RelationTemplet> getTempletBySrcServerTypes(
			List<String> srcServerTypes) {
		return templetRepository.findBySrcServerTypeIn(srcServerTypes);
	}

	@Override
	public Map<String, Object> getTempletPage(Integer pageNumber,
			Integer pageSize, RelationTemplet templet) {
		Map<String, Object> result = new HashMap<>();
		String hql = " FROM BASISDATA1.PLATFORM_RELATION_TEMPLET WHERE 1=1 ";
		if(StringUtils.isNotBlank(templet.getSrcServerType())){
			hql += " AND srcServerType='"+templet.getSrcServerType()+"'";
		}
		if(StringUtils.isNotBlank(templet.getDestServerType())){
			hql += " AND destServerType='"+templet.getDestServerType()+"'";
		}
		if(StringUtils.isNotBlank(templet.getSrcConsumerFc())){
			hql += " AND srcConsumerFc like '%"+templet.getSrcConsumerFc()+"%'";
		}
		if(StringUtils.isNotBlank(templet.getDestProviderFc())){
			hql += " AND destProviderFc like '%"+templet.getDestProviderFc()+"%'";
		}
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		List<RelationTemplet> templets = templetRepository.findPageByHQL(hql, pageInfo);
		result.put("rows", templets);
		result.put("total", Math.ceil((double)pageInfo.getTotal()/(double)pageInfo.getPageSize()));
		result.put("records", pageInfo.getTotal());
		return result;
	}

	@Override
	public RelationTemplet getTempletById(String id) {
		return templetRepository.getOne(id);
	}

	@Override
	public boolean saveTemplet(RelationTemplet templet) {
		try{
			if(StringUtils.isBlank(templet.getId())){
				templet.setId(UUIDTOOL.getuuid(32));
			}
			templetRepository.save(templet);
			return true;
		}catch(Exception e){
			logger.error("保存模板失败", e);
		}
		return false;
	}

	@Override
	public boolean deleteTemplet(String id) {
		try{
			templetRepository.delete(id);
			return true;
		}catch(Exception e){
			logger.error("删除模板失败", e);
		}
		return false;
	}

	@Override
	public Map<String, Object> getALLTempletFc() {
		Map<String, Object> result = new HashMap<>();
		List<RelationTemplet> templets = templetRepository.findAll();
		Set<String> consumerFc = new TreeSet<>();
		Set<String> providerFc = new TreeSet<>();
		for(RelationTemplet t : templets){
			consumerFc.add(t.getSrcConsumerFc());
			providerFc.add(t.getDestProviderFc());
		}
		return result;
	}

	@Override
	public boolean existTmeplet(RelationTemplet templet) {
		String hql = " FROM BASISDATA1.PLATFORM_RELATION_TEMPLET WHERE srcServerType='" + templet.getSrcServerType()
				   + "' AND srcConsumerFc='" + templet.getSrcConsumerFc()
				   + "' AND destServerType='" + templet.getDestServerType()
				   + "' AND destProviderFc='" + templet.getDestProviderFc() + "'";
		return (templetRepository.countByHQL(hql) > 0);
	}

	@Override
	public Map<String, Object> uploadExcelTemplet(List<MultipartFile> files) {
		Map<String, Object> result = new HashMap<>();
		result.put(ResultKeyConst.SUCESS_KEY, true);
		List<RelationTemplet> templetList = new ArrayList<>();
		List<RelationTemplet> templets = templetRepository.findAll();
		Set<String> templetSet = new HashSet<>();
		for(RelationTemplet t : templets){
			String tKey = t.getSrcServerType() + "_" + t.getSrcConsumerFc()
					+ "_" + t.getDestServerType() + "_" + t.getDestProviderFc();
			templetSet.add(tKey);
		}
		for(MultipartFile file : files){
			try {
				templetList.addAll(getTempletDataFromExcelFile(file, templetSet));
			} catch (IOException e) {
				result.put(ResultKeyConst.SUCESS_KEY, false);
				result.put(ResultKeyConst.MSG_KEY, String.format("读取文件[%s]异常", file.getName()));
			}
		}
		if(!templetList.isEmpty()){
			templetRepository.save(templetList);
		}

		result.put(ResultKeyConst.MSG_KEY, String.format("导入%s条数据", templetList.size()));

		return result;
	}

	private List<RelationTemplet> getTempletDataFromExcelFile(MultipartFile file, Set<String> existTempletKey) throws IOException{
		List<RelationTemplet> templetList = new ArrayList<>();
		try(POIFSFileSystem fs=new POIFSFileSystem(file.getInputStream());
				HSSFWorkbook wb=new HSSFWorkbook(fs)){

			int sheetnum = wb.getNumberOfSheets();
			// 循环sheet
			for(int n = 0; n < sheetnum; n++){
				HSSFSheet hssfSheet=wb.getSheetAt(n); // 获取第一个Sheet页
				int rowNum = hssfSheet.getPhysicalNumberOfRows();
				for(int i = 1; i <= rowNum; i++){
					HSSFRow hssfRow = hssfSheet.getRow(i);
					if(hssfRow==null){
						continue;
					}
					if(StringUtils.isNotBlank(formatCell(hssfRow.getCell(0)))){
						RelationTemplet templet = new RelationTemplet();
						templet.setId(UUIDTOOL.getuuid(32));
						templet.setSrcServerType(formatCell(hssfRow.getCell(0)));
						templet.setSrcConsumerFc(formatCell(hssfRow.getCell(1)));
						templet.setDestServerType(formatCell(hssfRow.getCell(2)));
						templet.setDestProviderFc(formatCell(hssfRow.getCell(3)));
						templet.setDescription(formatCell(hssfRow.getCell(4)));
						String tKey = templet.getSrcServerType() + "_" + templet.getSrcConsumerFc()
								+ "_" + templet.getDestServerType() + "_" + templet.getDestProviderFc();
						if(existTempletKey.add(tKey)){
							templetList.add(templet);
						}
					}
				}
			}
		}catch(IOException e){
			logger.debug("读取流向模板文件失败，", e);
			throw e;
		}

		return templetList;
	}

	@SuppressWarnings("deprecation")
	public String formatCell(HSSFCell hssfCell){
		if(hssfCell==null){
			return "";
		}else{
			if(hssfCell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
				return String.valueOf(hssfCell.getBooleanCellValue());
			}else if(hssfCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
				return String.valueOf(hssfCell.getNumericCellValue());
			}else{
				return String.valueOf(hssfCell.getStringCellValue());
			}
		}
	}

	@Override
	public void downloadTempletExcel(RelationTemplet templet,
			HttpServletResponse response) {
		String sql = " SELECT * FROM BASISDATA1.PLATFORM_RELATION_TEMPLET WHERE 1=1 ";
		if(StringUtils.isNotBlank(templet.getSrcServerType())){
			sql += " AND src_server_type='"+templet.getSrcServerType()+"'";
		}
		if(StringUtils.isNotBlank(templet.getDestServerType())){
			sql += " AND dest_server_type='"+templet.getDestServerType()+"'";
		}
		if(StringUtils.isNotBlank(templet.getSrcConsumerFc())){
			sql += " AND src_consumer_fc like '%"+templet.getSrcConsumerFc()+"%'";
		}
		if(StringUtils.isNotBlank(templet.getDestProviderFc())){
			sql += " AND dest_provider_fc like '%"+templet.getDestProviderFc()+"%'";
		}
		List<Map<String, Object>> dataSet = templetRepository.findMapPageBySQL(sql, null);
		String title = "流向模板导出文件";
		String[] headers = {"源服务类型", "源服务消费功能码", "目标服务类型", "目标服务功能码", "描述"};
		String[] columnNames = {"src_server_type", "src_consumer_fc", "dest_server_type", "dest_provider_fc", "description"};
		try (OutputStream out = response.getOutputStream()){
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(title, "UTF-8") + ".xls");
			ExportExcelUtil.exportExcel(title, headers, columnNames, dataSet, out, null);
		} catch (IOException e) {
			logger.error("流向模板导出异常", e);
		}
	}


}
