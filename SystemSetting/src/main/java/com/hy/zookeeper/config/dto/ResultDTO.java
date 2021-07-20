package com.hy.zookeeper.config.dto;

/**
 * 结果传输类
 * @author jianweng
 *
 */
public class ResultDTO {

	private boolean success;
	private String msg;
	private Object data;
	
	public ResultDTO() {
		super();
	}
	public ResultDTO(boolean success, String msg, Object data) {
		super();
		this.success = success;
		this.msg = msg;
		this.data = data;
	}
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
