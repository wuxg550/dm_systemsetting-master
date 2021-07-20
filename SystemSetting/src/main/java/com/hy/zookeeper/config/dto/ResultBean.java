package com.hy.zookeeper.config.dto;


import java.io.Serializable;

public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 返回消息状态码
     */
    private Integer code = 0;

    /***
     * 返回消息描述
     */
    private String message = "success";

    /**
     * 响应消息体
     */
    private T result;


    public ResultBean() {
    }

    public ResultBean(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultBean(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
        //code为0代表成功，有例外时另外设置
        this.success = (code==0);
    }

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
