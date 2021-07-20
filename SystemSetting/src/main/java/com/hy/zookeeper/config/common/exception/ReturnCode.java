package com.hy.zookeeper.config.common.exception;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.helpers.MessageFormatter;

/**
 * 返回码
 * 
 * @author ozz
 *
 */
public enum ReturnCode {

	
    //--------------------------外部返回编码-----------------------------
    
    SUCCESS(0, "结果执行成功"),
	JSON_PARAMETER_PARSE_ERROR(10002, "Json参数解析错误"),
	
	UNKNOWN_ERROR(99999, "未知错误，系统内部异常"),
	LOGASPECT_ERROR(99998, "切面调用方法错误，系统发生异常 "),
	NOFOUND_ERROR(99404, "404.{} Not Found."),
	TOO_MANY_REQUEST(110501, "服务请求过多"),
	REQUEST_HANDLE_FAIL(110503, "服务请求处理失败"),
	GET_WRITE_LOCK_FAIL(110508, "获取写操作锁失败"),
    OBJECT_VALIDATE_FAIL(110511, "请求参数校验不通过"),
    REQUEST_NOT_AFFECTED_ROW(110514, "请求影响0条数据");


	private final int code;
	private final String msgTemplate;

	ReturnCode(int code, String msgTemplate) {
		this.code = code;
		this.msgTemplate = msgTemplate;
	}

	public int code() {
		return code;
	}
	public String msgTemplate() {
		return msgTemplate;
	}

	public String getMsg(Object... args) {
		return MessageFormatter.arrayFormat(msgTemplate, args).getMessage();
	}

	public static ReturnCode getValue(int code) {
		for (ReturnCode returnCode : values()) {
			if (returnCode.code == code) {
				return returnCode;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return toString(ArrayUtils.EMPTY_OBJECT_ARRAY);
	}

	public String toString(Object... args) {
		return String.format("[%s %s] %s", code, name(), getMsg(args));
	}

}
