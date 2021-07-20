package com.hy.zookeeper.config.service;


/**
 * 功能节点服务层接口
 * @author jianweng
 *
 */
public interface IFunctionCodesService {

	/**
	 * functionCodes节点变更处理，
	 * 服务在线：将数据库上的功能码信息更新至functionCodes子节点，
	 * 服务离线：删除对应的functionCodes子节点
	 * @param serverId
	 */
	public void updateFunctionCodes(String serverId);
	
	/**
	 * 比对entrance节点功能码与数据库功能码，并删除或更新FunctionCodes子节点
	 * @param serverId
	 * @param entranceNodeData
	 */
	public void compareFcsAndUpdateFunctionCodes(String serverId, String entranceNodeData);
	
	/**
	 * 校验FunctionCodes子节点
	 * @param serverIpMap serverId:ip
	 * @param serverIdFcMap serverId_fc:port
	 */
	public void validateFunctionCodes();
}
