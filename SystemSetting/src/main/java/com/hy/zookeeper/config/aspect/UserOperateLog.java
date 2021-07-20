package com.hy.zookeeper.config.aspect;

import cn.hy.authori.common.utils.UUIDUtil;

/**
 * 日志记录的实体
 * 
 * @author ouzz
 * @date2019年9月10日
 */
public class UserOperateLog {

	private String id = UUIDUtil.getuuid();// 主键
	private String userorgname;// 用户所属机构
	private String userorgid;// 用户所属机构ID
	private String username;// 用户名(中文）
	private String userid;// 用户ID
	private String doobjectid;// 相关数据主键
	private String dodetail;// 操作详情（新增流向模板）
	private String dotypename;// 操作类型（新增，修改，删除）
	private String resultdata;// 返回报文
	private String paramdata;// 请求报文
	private String requertip;// 客户端IP：192.168.15.46
	private String logtype = "log_UserOperate";// 日志类型，默认：log_UserOperate
	private String logtypename = "用户操作日志";// 日志类型名称，默认：用户操作日志
	private String logtime;// 操作时间

	public UserOperateLog(String doobjectid, String dodetail, String dotypename,String resultdata, String paramdata, String requertip, String logtime) {
		this.doobjectid = doobjectid;
		this.dodetail = dodetail;
		this.dotypename = dotypename;
		this.resultdata = resultdata;
		this.paramdata = paramdata;
		this.requertip = requertip;
		this.logtime = logtime;
	}

	public UserOperateLog() {
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getUserorgname() {
		return userorgname;
	}


	public void setUserorgname(String userorgname) {
		this.userorgname = userorgname;
	}


	public String getUserorgid() {
		return userorgid;
	}

	public void setUserorgid(String userorgid) {
		this.userorgid = userorgid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getDoobjectid() {
		return doobjectid;
	}

	public void setDoobjectid(String doobjectid) {
		this.doobjectid = doobjectid;
	}

	public String getDodetail() {
		return dodetail;
	}

	public void setDodetail(String dodetail) {
		this.dodetail = dodetail;
	}

	public String getDotypename() {
		return dotypename;
	}

	public void setDotypename(String dotypename) {
		this.dotypename = dotypename;
	}

	public String getResultdata() {
		return resultdata;
	}

	public void setResultdata(String resultdata) {
		this.resultdata = resultdata;
	}

	public String getParamdata() {
		return paramdata;
	}

	public void setParamdata(String paramdata) {
		this.paramdata = paramdata;
	}

	public String getRequertip() {
		return requertip;
	}

	public void setRequertip(String requertip) {
		this.requertip = requertip;
	}

	public String getLogtype() {
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getLogtypename() {
		return logtypename;
	}

	public void setLogtypename(String logtypename) {
		this.logtypename = logtypename;
	}

	public String getLogtime() {
		return logtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
}
