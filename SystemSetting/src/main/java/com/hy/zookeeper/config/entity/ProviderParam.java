package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 服务提供功能表-子表（参数元数据）
 * @author Administrator
 *
 */
//@Entity
//@Table(name="platform_server_provider_param")
public class ProviderParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String groupId;//	分组id
	private String field;//	字段
	private String fieldCh;//	字段中文解释
	private String dataType;//	数据类型
	private String fixedValue;//	固定值
	private String enumGroup;//	枚举集合
	private Integer isMultiSelect;//	可否多选
	private Integer maxLength;//	数据最大长度
	private Integer minVal;//	最小值
	private Integer maxVal;//	最大值
	private Integer isNotNull;//	是否必填
	private Integer isCallbackFlag;//	回调标记
	private Integer isAutoCallbackFlag;//	回调标记是否自动生成
	private Integer fieldType;//	字段类型
	private String remark;//	备注
	
	@Id 
	@Column(name ="group_id",nullable=false) 
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@Column(name ="field") 
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	@Column(name ="field_ch") 
	public String getFieldCh() {
		return fieldCh;
	}
	public void setFieldCh(String fieldCh) {
		this.fieldCh = fieldCh;
	}
	@Column(name ="data_type") 
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Column(name ="fixed_value") 
	public String getFixedValue() {
		return fixedValue;
	}
	public void setFixedValue(String fixedValue) {
		this.fixedValue = fixedValue;
	}
	@Column(name ="enum_group") 
	public String getEnumGroup() {
		return enumGroup;
	}
	public void setEnumGroup(String enumGroup) {
		this.enumGroup = enumGroup;
	}
	@Column(name ="is_multi_select") 
	public Integer getIsMultiSelect() {
		return isMultiSelect;
	}
	public void setIsMultiSelect(Integer isMultiSelect) {
		this.isMultiSelect = isMultiSelect;
	}
	@Column(name ="max_length") 
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	@Column(name ="min_val") 
	public Integer getMinVal() {
		return minVal;
	}
	public void setMinVal(Integer minVal) {
		this.minVal = minVal;
	}
	@Column(name ="max_val") 
	public Integer getMaxVal() {
		return maxVal;
	}
	public void setMaxVal(Integer maxVal) {
		this.maxVal = maxVal;
	}
	@Column(name ="is_not_null") 
	public Integer getIsNotNull() {
		return isNotNull;
	}
	public void setIsNotNull(Integer isNotNull) {
		this.isNotNull = isNotNull;
	}
	@Column(name ="is_callback_flag") 
	public Integer getIsCallbackFlag() {
		return isCallbackFlag;
	}
	public void setIsCallbackFlag(Integer isCallbackFlag) {
		this.isCallbackFlag = isCallbackFlag;
	}
	@Column(name ="is_auto_callback_flag") 
	public Integer getIsAutoCallbackFlag() {
		return isAutoCallbackFlag;
	}
	public void setIsAutoCallbackFlag(Integer isAutoCallbackFlag) {
		this.isAutoCallbackFlag = isAutoCallbackFlag;
	}
	@Column(name ="field_type") 
	public Integer getFieldType() {
		return fieldType;
	}
	public void setFieldType(Integer fieldType) {
		this.fieldType = fieldType;
	}
	@Column(name ="remark") 
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
