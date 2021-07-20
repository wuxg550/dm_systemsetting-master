package com.hy.zookeeper.config.aspect;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户操作日志常量
 * 
 * @author ouzz
 * @date2019年11月13日
 */
public class UserOperateLogConst {

	private UserOperateLogConst() {
	}

	// 操作类型
	public static final String SAVE_DETAIL = "新增";
	public static final String UPDATE_DETAIL = "修改";
	public static final String DELETE_DETAIL = "删除";
	public static final String FILE_DETAIL = "文件上传";

	// 分割符号
	public static final String SPLIT = ".";
	public static final String DETAIL_SPLIT = "->";

	private static String serverInfoController = "ServerInfoController";
	private static String relationController = "RelationController";
	private static String relationTempletController = "RelationTempletController";
	private static String serverTypeController = "ServerTypeController";
	private static String entranceController = "EntranceController";
	private static String agreementTypeController = "AgreementTypeController";

	private static String serverType = "服务类型";
	private static String agreementType = "协议类型";
	private static String serverInfo = "配置服务列表";
	private static String entrance = "配置服务入口列表";
	private static String commonServerInfo = "服务公有信息配置";
	private static String relation = "流程列表";
	private static String relationTemplet = "模板列表";

	public static final String SAVE_OR_UPDATE = "新增/修改";
	public static final String DEL = "删除";

	private static String batchDelete = "batchDelete";

	public static final String SERVER_IDS = "serverIds";
	public static final String ID = "id";
	public static final String FILE = "file";

	/**
	 * 操作日志map key:记录类名+方法名 value：方法实现的操作详情说明
	 */
	protected static final Map<String, String> USER_OPERATE_MAP;  
    static  
    {  
    	USER_OPERATE_MAP = new HashMap<>();  
        // 服务类型操作
		USER_OPERATE_MAP.put(serverTypeController + SPLIT + "savaServerType", serverType + DETAIL_SPLIT + SAVE_OR_UPDATE);
		USER_OPERATE_MAP.put(serverTypeController + SPLIT + "delServerType", serverType + DETAIL_SPLIT + DEL);
		// 协议类型操作
		USER_OPERATE_MAP.put(agreementTypeController + SPLIT + "saveAgreementType", agreementType + DETAIL_SPLIT + SAVE_OR_UPDATE);
		USER_OPERATE_MAP.put(agreementTypeController + SPLIT + "delete", agreementType + DETAIL_SPLIT + DEL);
		// 配置服务列表
		USER_OPERATE_MAP.put(serverInfoController + SPLIT + "saveServer", serverInfo + DETAIL_SPLIT + SAVE_OR_UPDATE);
		USER_OPERATE_MAP.put(serverInfoController + SPLIT + "delServer", serverInfo + DETAIL_SPLIT + DEL);
		USER_OPERATE_MAP.put(relationController + SPLIT + "autoConfig", serverInfo + DETAIL_SPLIT + "一键配置流向");
		USER_OPERATE_MAP.put(relationController + SPLIT + "imposedConfig", serverInfo + DETAIL_SPLIT + "强制配置流向");
		USER_OPERATE_MAP.put("NodeDataController" + SPLIT + "validateNodeData", serverInfo + DETAIL_SPLIT + "同步数据");
		USER_OPERATE_MAP.put(serverInfoController + SPLIT + batchDelete, serverInfo + DETAIL_SPLIT + "删除流线服务");
		USER_OPERATE_MAP.put(serverInfoController + SPLIT + "registeThirdPartyServer", serverInfo + DETAIL_SPLIT + "注册第三方服务");
		USER_OPERATE_MAP.put(serverInfoController + SPLIT + "saveRunningConfig", serverInfo + DETAIL_SPLIT + "运行配置");
		// 配置服务入口列表
		USER_OPERATE_MAP.put(entranceController + SPLIT + "saveEntrance", entrance + DETAIL_SPLIT + SAVE_OR_UPDATE);
		USER_OPERATE_MAP.put(entranceController + SPLIT + "delEntrance", entrance + DETAIL_SPLIT + DEL);
		// 服务公有信息配置
		USER_OPERATE_MAP.put(serverInfoController + SPLIT + "saveCommonServerInfo", commonServerInfo + DETAIL_SPLIT + "运行配置");
		// 流向列表
		USER_OPERATE_MAP.put(relationController + SPLIT + "saveRelation", relation + DETAIL_SPLIT + SAVE_OR_UPDATE);
		USER_OPERATE_MAP.put(relationController + SPLIT + "deleteRelation", relation + DETAIL_SPLIT + DEL);
		USER_OPERATE_MAP.put(relationController + SPLIT + "deleteRelationByServerId", relation + DETAIL_SPLIT + "根据源服务id删除");
		USER_OPERATE_MAP.put(relationController + SPLIT + "validateRelation", relation + DETAIL_SPLIT + "校验流向");
		USER_OPERATE_MAP.put(relationController + SPLIT + batchDelete, relation + DETAIL_SPLIT + "批量删除");
		USER_OPERATE_MAP.put(relationController + SPLIT + "handleFileUpload", relation + DETAIL_SPLIT + "上传文件");
		// 模板列表
		USER_OPERATE_MAP.put(relationTempletController + SPLIT + "saveTemplet", relationTemplet + DETAIL_SPLIT + SAVE_OR_UPDATE);
		USER_OPERATE_MAP.put(relationTempletController + SPLIT + "deleteTemplet", relationTemplet + DETAIL_SPLIT + DEL);
		USER_OPERATE_MAP.put(relationTempletController + SPLIT + "uploadExcel", relationTemplet + DETAIL_SPLIT + "导入");
    }  
	/**
	 * 操作日志关键主键map key:记录类名+方法名 value：关键主键
	 */
    protected static final Map<String, String> USER_OPERATE_ID_MAP;  
    static  
    {  
    	USER_OPERATE_ID_MAP = new HashMap<>();  
		// 服务类型操作
		USER_OPERATE_ID_MAP.put(serverTypeController + SPLIT + "savaServerType", ID);
		USER_OPERATE_ID_MAP.put(serverTypeController + SPLIT + "delServerType", ID);
		// 协议类型操作
		USER_OPERATE_ID_MAP.put(agreementTypeController + SPLIT + "saveAgreementType", ID);
		USER_OPERATE_ID_MAP.put(agreementTypeController + SPLIT + "delete", ID);
		// 配置服务列表
		USER_OPERATE_ID_MAP.put(serverInfoController + SPLIT + "saveServer", ID);
		USER_OPERATE_ID_MAP.put(serverInfoController + SPLIT + "delServer", ID);
		USER_OPERATE_ID_MAP.put(relationController + SPLIT + "autoConfig", SERVER_IDS);
		USER_OPERATE_ID_MAP.put(relationController + SPLIT + "imposedConfig", SERVER_IDS);
		USER_OPERATE_ID_MAP.put(serverInfoController + SPLIT + batchDelete, SERVER_IDS);
		USER_OPERATE_ID_MAP.put(serverInfoController + SPLIT + "saveRunningConfig", ID);
		// 配置服务入口列表
		USER_OPERATE_ID_MAP.put(entranceController + SPLIT + "saveEntrance", ID);
		USER_OPERATE_ID_MAP.put(entranceController + SPLIT + "delEntrance", "entranceId");
		// 流向列表
		USER_OPERATE_ID_MAP.put(relationController + SPLIT + "saveRelation", ID);
		USER_OPERATE_ID_MAP.put(relationController + SPLIT + "deleteRelation", ID);
		USER_OPERATE_ID_MAP.put(relationController + SPLIT + "deleteRelationByServerId", SERVER_IDS);
		USER_OPERATE_ID_MAP.put(relationController + SPLIT + batchDelete, "ids");
		USER_OPERATE_ID_MAP.put(relationController + SPLIT + "handleFileUpload", FILE);
		// 模板列表
		USER_OPERATE_ID_MAP.put(relationTempletController + SPLIT + "saveTemplet", ID);
		USER_OPERATE_ID_MAP.put(relationTempletController + SPLIT + "deleteTemplet", ID);
		USER_OPERATE_ID_MAP.put(relationTempletController + SPLIT + "uploadExcel", FILE);
		// 服务公有信息配置
		USER_OPERATE_ID_MAP.put(serverInfoController + SPLIT + "saveCommonServerInfo", "orgId");
	}

}
