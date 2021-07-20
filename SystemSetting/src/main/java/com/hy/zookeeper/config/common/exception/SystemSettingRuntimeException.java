package com.hy.zookeeper.config.common.exception;

import org.springframework.core.NestedRuntimeException;

public class SystemSettingRuntimeException extends NestedRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8379059740123554340L;

	private final int code;
	private final String shortMessage;
	
	public int getCode() {
		return code;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public SystemSettingRuntimeException(ReturnCode returnCode) {
		this(returnCode.code(), returnCode.msgTemplate());
	}

	public SystemSettingRuntimeException(ReturnCode returnCode, Throwable cause) {
		this(returnCode.code(), returnCode.msgTemplate(), cause);
	}
	
	public SystemSettingRuntimeException(int code, String shortMessage) {
		super(shortMessage);
		this.code = code;
		this.shortMessage = shortMessage;
	}

	public SystemSettingRuntimeException(int code, String shortMessage, Throwable cause) {
		super(shortMessage, cause);
		this.code = code;
		this.shortMessage = shortMessage;
	}
}
