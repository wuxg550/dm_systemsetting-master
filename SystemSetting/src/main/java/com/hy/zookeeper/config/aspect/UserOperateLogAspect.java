package com.hy.zookeeper.config.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.hy.authori.sdk.api.AuthApi;
import cn.hy.authori.user.dto.UserDTO;

import com.hy.zookeeper.config.common.exception.ReturnCode;
import com.hy.zookeeper.config.common.exception.SystemSettingRuntimeException;
import com.hy.zookeeper.config.util.HYJsonUtil;

@Aspect
@Component
public class UserOperateLogAspect {

	// token在Header的属性名
	private static final String TOKEN_ON_HEADER_KEY = "Authorization";
	// token在Header中的前缀
	private static final String TOKEN_HEADER_PERFIX = "Bearer ";

	private Logger logger = LoggerFactory.getLogger(UserOperateLogAspect.class);
	private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Pointcut(value = "execution(public * com.hy.zookeeper.config.controller.*.*(..))")
	private void methodPointcut() {
		// methodLevel pointcut,nothing to do
	}

	@Autowired
	private AuthApi authApi;

	@Around("methodPointcut()")
	public Object around(ProceedingJoinPoint point) {
		String logTime = dateformat.format(new Date());// 执行时间
		// 执行方法
		Object result;
		try {
			result = point.proceed();
		} catch (Throwable e1) {
			throw new SystemSettingRuntimeException(ReturnCode.LOGASPECT_ERROR, e1);
		}
		Signature signature = point.getSignature();
		String className = signature.getDeclaringType().getSimpleName();// 类名
		String methodName = signature.getName(); // 方法名
		// 用户操作日志校验:只记录需要记录的操作,不需要记录的操作动作直接返回。
		String logkey = className + UserOperateLogConst.SPLIT + methodName;
		if (!UserOperateLogConst.USER_OPERATE_MAP.containsKey(logkey)) {
			return result;
		}
		UserOperateLog userOperateLog = new UserOperateLog();
		try {
			userOperateLog.setResultdata(HYJsonUtil.createJson(result));// 返回报文
			// 通过header获取登陆用户的token
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			userOperateLog.setRequertip(request.getRemoteAddr());// 客户端IP
			String doDetail = UserOperateLogConst.USER_OPERATE_MAP.get(logkey);
			userOperateLog.setDodetail(doDetail);// 操作详情
			Map<String, String[]> paramMap = request.getParameterMap();// 请求报文
			String paramData = HYJsonUtil.createJson(paramMap);
			String doObjectId = "";// 相关数据主键
			// 操作类型（新增，修改，删除）
			String doTypeName = UserOperateLogConst.UPDATE_DETAIL;
			if (UserOperateLogConst.USER_OPERATE_ID_MAP.containsKey(logkey)) {
				String idKey = UserOperateLogConst.USER_OPERATE_ID_MAP.get(logkey);
				if (UserOperateLogConst.FILE.equals(idKey)) {
					// 请求报文：接口为上传文件，记录上传文件名称。(取第一个文件名）
					paramData = request.getParts().iterator().next().getSubmittedFileName();
					doTypeName = UserOperateLogConst.FILE_DETAIL;
				} else {
					// 请求报文
					doObjectId = HYJsonUtil.createJson(paramMap.get(idKey));
					if (doDetail.substring(doDetail.length() - UserOperateLogConst.DEL.length(), doDetail.length()).equals(UserOperateLogConst.DEL)) {
						doTypeName = UserOperateLogConst.DELETE_DETAIL;
					} else if (doDetail.substring(doDetail.length() - UserOperateLogConst.SAVE_OR_UPDATE.length(), doDetail.length()).equals(
							UserOperateLogConst.SAVE_OR_UPDATE)
							&& doObjectId.isEmpty()) {
						doTypeName = UserOperateLogConst.SAVE_DETAIL;
					}
				}
			}
			userOperateLog.setParamdata(paramData);// 请求报文
			userOperateLog.setDotypename(doTypeName);// 操作类型
			userOperateLog.setLogtime(logTime);// 执行时间
			userOperateLog.setDoobjectid(doObjectId);// 相关数据主键
			String token = request.getHeader(TOKEN_ON_HEADER_KEY).replace(TOKEN_HEADER_PERFIX, StringUtils.EMPTY).trim();
			// 通过token获取用户信息
			UserDTO userDTO = authApi.getCurrentUser(token);
			userOperateLog.setUserid(getUserBykey(userDTO.getAccount(), "id"));// 用户ID
			userOperateLog.setUsername(getUserBykey(userDTO.getAccount(), "username") + "(" + getUserBykey(userDTO.getAccount(), "loginname") + ")");// 用户名+登录名
			userOperateLog.setUserorgid(getUserBykey(userDTO.getOrganization(), "id"));// 用户所属机构ID
			userOperateLog.setUserorgname(getUserBykey(userDTO.getOrganization(), "name"));// 用户所属机构
			String content = HYJsonUtil.createJson(userOperateLog);
			logger.info(content);
		} catch (Exception e) {
			logger.error("用户操作日志记录失败,{}", HYJsonUtil.createJson(userOperateLog), e);
		}
		return result;
	}

	// 根据key查找用户信息
	private String getUserBykey(Map<String, Object> map, String key) {
		if (map != null && MapUtils.isNotEmpty(map) && map.containsKey(key)) {
			return String.valueOf(map.get(key));
		} else {
			return "";
		}
	}
}
