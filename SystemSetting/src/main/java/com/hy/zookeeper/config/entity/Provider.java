package com.hy.zookeeper.config.entity;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 服务提供功能表（功能元数据）
 * @author hrh
 *
 */
//@Entity
//@Table(name="platform_server_provider")
public class Provider implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//	主键
	private String fc;//	功能码
	private String serverType;//	服务类型
	private String entranceId;//	入口id
	private String funType;//	功能类型
	private String pageUrl;//	界面地址
	private String funDescribe;//	功能描述
	private String funAppointedDev;//	功能指定的设备类型
	private String reqParamGroupId;//	输入参数组id
	private String reqParamModel;//	输入参数模板
	private String retParamGroupId;//	返回参数组id
	private String retParamModel;//	返回参数模板
	private String cbParamGroupId;//	回调参数组id
	private String cbParamModel;//	回调参数模板
	
	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name ="fc")
	public String getFc() {
		return fc;
	}
	public void setFc(String fc) {
		this.fc = fc;
	}
	@Column(name ="server_type")
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	@Column(name ="entrance_id")
	public String getEntranceId() {
		return entranceId;
	}
	public void setEntranceId(String entranceId) {
		this.entranceId = entranceId;
	}
	@Column(name ="fun_type")
	public String getFunType() {
		return funType;
	}
	public void setFunType(String funType) {
		this.funType = funType;
	}
	@Column(name ="page_url")
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	@Column(name ="fun_describe")
	public String getFunDescribe() {
		return funDescribe;
	}
	public void setFunDescribe(String funDescribe) {
		this.funDescribe = funDescribe;
	}
	@Column(name ="fun_appointed_dev")
	public String getFunAppointedDev() {
		return funAppointedDev;
	}
	public void setFunAppointedDev(String funAppointedDev) {
		this.funAppointedDev = funAppointedDev;
	}
	@Column(name ="req_param_group_id")
	public String getReqParamGroupId() {
		return reqParamGroupId;
	}
	public void setReqParamGroupId(String reqParamGroupId) {
		this.reqParamGroupId = reqParamGroupId;
	}
	@Column(name ="req_param_model")
	public String getReqParamModel() {
		return reqParamModel;
	}
	public void setReqParamModel(String reqParamModel) {
		this.reqParamModel = reqParamModel;
	}
	
	@Column(name ="ret_param_group_id")
	public String getRetParamGroupId() {
		return retParamGroupId;
	}
	public void setRetParamGroupId(String retParamGroupId) {
		this.retParamGroupId = retParamGroupId;
	}
	@Column(name ="ret_param_model")
	public String getRetParamModel() {
		return retParamModel;
	}
	public void setRetParamModel(String retParamModel) {
		this.retParamModel = retParamModel;
	}
	@Column(name ="cb_param_group_id")
	public String getCbParamGroupId() {
		return cbParamGroupId;
	}
	public void setCbParamGroupId(String cbParamGroupId) {
		this.cbParamGroupId = cbParamGroupId;
	}
	@Column(name ="cb_param_model")
	public String getCbParamModel() {
		return cbParamModel;
	}
	public void setCbParamModel(String cbParamModel) {
		this.cbParamModel = cbParamModel;
	}
	
	
	
}
