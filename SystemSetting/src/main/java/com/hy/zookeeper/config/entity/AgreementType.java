package com.hy.zookeeper.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 服务协议类型
 * @author hrh
 *
 */
@Entity
@Table(name="BASISDATA1.platform_agreement_type")
public class AgreementType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7141960696440861440L;
	
	
	private String id;
	
	private String type;
	
	private String remarks;

	
	@Id 
	@Column(name ="id",nullable=false) 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name ="type") 
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name ="remarks") 
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	

}
