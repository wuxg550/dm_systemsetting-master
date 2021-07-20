package com.hy.zookeeper.config.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.dto.CommonServerInfo;
import com.hy.zookeeper.config.dto.ResultDTO;
import com.hy.zookeeper.config.entity.RunningConfig;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.enums.ConfigFlagEnum;
import com.hy.zookeeper.config.service.IServerInfoService;

@Controller
@RequestMapping("/serverInfo")
public class ServerInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerInfoController.class);
	
	@Autowired
	private IServerInfoService serverInfoService;
	
	@RequestMapping(value="/configServerList",method=RequestMethod.GET)
	public ModelAndView indexs(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView("server/configServerList");
	}
	
	@RequestMapping(value="/configServerEdit",method=RequestMethod.GET)
	public ModelAndView configServerEdit(HttpServletRequest request,HttpServletResponse reponse){
		return new ModelAndView("server/configServerEdit");
	}
	
	
	@RequestMapping("/getServerPage")
	@ResponseBody
	public Map<String,Object> getAllServer(int page, int rows, ServerInfo ifno){
		return serverInfoService.findAllPage(page,rows, ifno);
	}
	
	@RequestMapping("/getServer")
	@ResponseBody
	public List<ServerInfo> getAllServer(){
		
		return serverInfoService.findAll();
	}
	
	
	@RequestMapping("/saveServer")
	@ResponseBody
	public boolean saveServer(ServerInfo serverInfo){
		return serverInfoService.saveServer(serverInfo);
	}
	
	@RequestMapping(value="/updataServer",method=RequestMethod.GET)
	public ModelAndView serverInfoUpdate(HttpServletRequest request,String id){
		ModelAndView mv = new ModelAndView("server/configServerEdit");
		if(id != null){
			ServerInfo serverInfo = serverInfoService.getServerById(id);
			mv.addObject("serverInfo",serverInfo);
		}
		return mv;
	}
	
	@RequestMapping("/delServer")
	@ResponseBody
	public Map<String,Object> delServer(String id){
		return serverInfoService.deleteServer(id);
	}

	@RequestMapping("/updServer")
	@ResponseBody
	public int updateServer(String id,String status,String cumlum ){
		return serverInfoService.updateStatus(id, status,cumlum);
	}
	
	@RequestMapping("/getServerSelectData")
	@ResponseBody
	public Map<String, Object> getServerSelectData(){
		return serverInfoService.getServerSelectData();
	}
	
	@RequestMapping("/getOneServer")
	@ResponseBody
	public ServerInfo getOneServers(String id){
		return serverInfoService.getServerById(id);
	}
	
	@RequestMapping(value="/runningConfig",method=RequestMethod.GET)
	public ModelAndView runningConfig(HttpServletRequest request,String id){
		ModelAndView mv = new ModelAndView("server/runningConfig");
		mv.addObject("serverId",id);
		if(id != null){
			ServerInfo serverInfo = serverInfoService.getServerById(id);
			mv.addObject("serverName", serverInfo.getServerName());
		}
		return mv;
	}
	
	@RequestMapping("/getRunningConfig")
	@ResponseBody
	public String getRunningConfig(String id){
		Map<String, Object> result = new HashMap<>(); 
		List<RunningConfig> data = serverInfoService.getRunningConfig(id);
		result.put("configData", data); 
		result.put("serverId", id);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping("/saveRunningConfig")
	@ResponseBody
	public String saveRunningConfig(String configData, String id){
		Map<String, Object> result = new HashMap<>();
		try{
			serverInfoService.saveRunningConfig(configData, id);
			result.put("success", true);
		}catch(Exception e){
			result.put("success", false);
			logger.error("保存运行配置异常", e);
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value="/commonServerInfo",method=RequestMethod.GET)
	public ModelAndView commonServerInfo(HttpServletRequest request,String id){
		ModelAndView mv = new ModelAndView("server/commonServerInfo");
		mv.addObject("infos", serverInfoService.getCommonServerInfo());
		return mv;
	}
	
	@RequestMapping("/getCommonServerInfo")
	@ResponseBody
	public CommonServerInfo getCommonServerInfo(){
		return serverInfoService.getCommonServerInfo();
	}
	
	@RequestMapping("/saveCommonServerInfo")
	@ResponseBody
	public Map<String, Object> saveCommonServerInfo(CommonServerInfo info){
		return serverInfoService.saveCommonServerInfo(info);
	}
	
	@RequestMapping("/getServerDetail")
	@ResponseBody
	public Map<String, Object> getServerDetail(String serverId){
		return serverInfoService.getServerDatial(serverId);
	}
	
	@RequestMapping(value="/delOfflineServer", method=RequestMethod.DELETE)
	@ResponseBody
	public ResultDTO delOfflineServer(){
		ResultDTO result = new ResultDTO();
		try{
			int count = serverInfoService.delOfflineServer();
			return new ResultDTO(true, "删除成功", count);
		}catch(Exception e){
			result.setSuccess(false);
			logger.error("删除离线服务异常", e);
		}
		return result;
	}
	
	@RequestMapping(value="/batchDelete", method=RequestMethod.DELETE)
	@ResponseBody
	public ResultDTO batchDelete(String serverIds){
		if(StringUtils.isBlank(serverIds)){
			return new ResultDTO(false, "未勾选服务", null);
		}
		int count = serverInfoService.deleteServers(serverIds);
		return new ResultDTO(true, "删除成功", count);
	}
	
	@RequestMapping(value="/getConfigFlagEnum")
	@ResponseBody
	public ResultDTO getConfigFlagEnum(){
		return new ResultDTO(true, "", ConfigFlagEnum.getMap());
	}
	
	@RequestMapping(value="/registeThirdPartyServer")
	@ResponseBody
	public void registeThirdPartyServer(){
		serverInfoService.registeThirdPartyServer();
	}
}
