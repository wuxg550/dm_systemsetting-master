package com.hy.zookeeper.config.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.hy.zookeeper.config.dto.ResultBean;
import com.hy.zookeeper.config.entity.ServerRelation;

/**
 * 流向配置业务层
 * @author jianweng
 *
 */
public interface IServerRelationService {

	/**
	 * 通过ServerId串获取流向集合
	 * @param serverIds
	 * @return
	 */
	public List<ServerRelation> getRelationByServerIds(List<String> serverIds);
	
	/**
	 * ip改变
	 * @param serverId 源服务Id
	 * @param ip 现ip
	 * @return
	 */
	public boolean ipChange(String serverId, String ip);
	
	/**
	 * 入口节点改变
	 * @param serverType 改变的服务的服务类型
	 * @param serverId 改变的服务的服务ID
	 * @param entranceList 现该服务入口集合
	 * @return
	 * 流向更改思路
	 * 1、根据serverId获取所有依赖此服务的流向集合
	 * 2、更新该服务数据库入口数据
	 * 3、根据EntranceDto集合中的功能码修改步骤1中的流向集合
	 * 4、保存流向集合至数据库
	 * 5、根据流向集合更改zookeeper服务上relation节点
	 */
	public boolean entranceChange(String serverType, String serverId, String entranceJsonStr);
	
	
	/**
	 * 新增流向
	 * @param relation
	 * @return
	 */
	public boolean saveRelation(ServerRelation relation);
	
	/**
	 * 批量新增流向
	 * @param relationList
	 * @return
	 */
	public boolean batchSave(List<ServerRelation> relationList);
	
	/**
	 * 导入流向
	 * @param files 流向文件
	 * @param connectString
	 * @return
	 */
	public String importRelation(MultipartFile files);
	
	/**
	 * 通过id查询流向
	 * @param ids
	 * @return
	 */
	public ServerRelation getRelationById(String id);
	
	/**
	 * 一键配置流向，根据模板功能码判断流向有无，有则不覆盖，无则添加
	 * @param serverTypes 需要配置流向的服务类型集合
	 * @param serverIds 需要配置流向的服务ID集合
	 * @return
	 */
	public boolean configRelationByTemple(List<String> serverTypes, List<String> serverIds);
	
	public boolean configRelationByTemple(List<String> serverTypes, List<String> serverIds, String destServerType);
	
	/**
	 * 强制配置流向，覆盖所有流向
	 * @param serverTypes 需要配置流向的服务类型集合
	 * @param serverIds 需要配置流向的服务ID集合
	 * @return
	 */
	public boolean imposedConfingRelation(List<String> serverTypes, List<String> serverIds);
	
	/**
	 * 验证zookeeper节点下流向的ip及端口与配置中心数据库是否一致
	 * @param connectString
	 */
	public void validateRelation();
	
	/**
	 * 分页获取流向列表
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> getRelationPage(Integer pageNumber, Integer pageSize, ServerRelation relation);
	
	/**
	 * 根据数据库数据修改流向节点数据
	 * @param serverType 服务类型
	 * @param serverId 服务ID
	 * @return
	 */
	public boolean editRelationNode(String serverType, String serverId);
	
	/**
	 * 根据数据库数据修改流向节点数据
	 * @param serverPathMap key-value:serverIds-path
	 * @return
	 */
	public boolean editRelationNode(Map<String, String> serverPathMap);
	
	/**
	 * 获取所有流向数据
	 * @return
	 */
	public Map<String, Object> getServerRelationTree(Integer pageNumber,
                                                     Integer pageSize, ServerRelation relation);
	
	/**
	 * 更新流向，先删除全部再保存
	 * @param ServerId
	 * @param relationList
	 * @return
	 */
	public boolean updateRelation(String serverId, List<ServerRelation> relationList);
	
	/**
	 * 根据流向id删除流向
	 * @param id
	 * @return
	 */
	public boolean deleteRelation(String id);
	
	/**
	 * 根据服务id删除流向
	 * @param serverId
	 * @return
	 */
	public boolean deleteRelationByServerId(String serverId);
	
	/**
	 * 根据目标服务id删除流向数据，并同步更新zookeeper节点
	 * @param serverId
	 * @return
	 */
	public boolean deleteRelationByDestServerId(String serverId);
	
	
	/**
	 * 导出流向列表
	 * @param response
	 */
	public void exportRelation(HttpServletResponse response);
	
	/**
	 * 获取流向导图数据
	 * @param serverIds
	 * @return
	 */
	public Map<String, Object> getRelationMap(List<String> serverIds);
	
	/**
	 * 
	 * @param serverIds
	 * @return
	 */
	public Map<String, Object> deleteRelationByLine(String lineId);
	
	public Map<String, Object> addRelationLine(String srcNode, String destNode, String serverIds);
	
	/**
	 * 用于服务注册入口时配置流向
	 * @param destServerType 目标服务类型
	 */
	void configRelation(String destServerType);
	
	/**
	 * 批量删除流向
	 * @param ids
	 * @return
	 */
	Map<String, Object> deleteRelationByIds(String ids);
	
	void deleteBySrcServerId(String srcServerId);
	
	/**
	 * 服务在离线变更流向
	 * @param destServerId
	 * @param onlineStatu
	 */
	void serverOnOffLine(String destServerId, Integer onlineStatu);
	
	ResultBean<String> generateRelationByServerType(List<String> destServerType);
}
