package com.hy.zookeeper.config.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hy.zookeeper.config.dto.ServerEntranceDto;
import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.service.IServerEntranceService;

@Controller
@RequestMapping("/entrance")
public class EntranceController {
	
	@Autowired
	private IServerEntranceService serverEntranceService;
	
	@RequestMapping(value="/entranceList",method=RequestMethod.GET)
	public ModelAndView indexs(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView("server/serverEntranceList");
	}
	
	@RequestMapping(value="/entranceEdit",method=RequestMethod.GET)
	public ModelAndView entranceEdit(HttpServletRequest request, HttpServletResponse response,String id,String fc) {
		 ModelAndView mv = new ModelAndView("server/entranceEdit");
		 request.setAttribute("serverId", id+":"+fc);
		 return mv;
	}

	@RequestMapping("/getTableData")
	@ResponseBody
	public List<ServerEntranceDto> getServerEntranceList(HttpServletRequest request,String serverType,String serverId){
		return serverEntranceService.getEntranceList(serverType, serverId);
	}
	
	@RequestMapping("/saveEntrance")
	@ResponseBody
	public boolean saveEntrance(ServerEntrance serverEntrance){
		
		return serverEntranceService.saveEntrance(serverEntrance);
	}
	
	@RequestMapping("/getEntrance")
	@ResponseBody
	public ServerEntrance getOneServerEntrance(HttpServletRequest request,String entranceId){
		return serverEntranceService.findByEntranceId(entranceId);
	}
	
	@RequestMapping("/delEntrance")
	@ResponseBody
	public boolean delEntrance(String entranceId){
		return serverEntranceService.delEntrance(entranceId, "");
	}
	
	@RequestMapping("/getEntranceList")
	@ResponseBody
	public Map<String,Object> getEntranceList(int page,int rows,String serverId){
		return serverEntranceService.getEntrance(page,rows,serverId);
	}
	
	
	@RequestMapping(value="/serverEntranceEdit",method=RequestMethod.GET)
	public ModelAndView getServerEntranceDtoById(HttpServletRequest request, HttpServletResponse response,String id,String info){
		ModelAndView mv = new ModelAndView("server/entranceEdit");
		request.setAttribute("serverEntranceDto",serverEntranceService.getServerEntranceDtoById(id) );
		request.setAttribute("info", info);
		return mv;
	}
	
	@RequestMapping("/getFcs")
	@ResponseBody
	public Object getFcs(String serverId){
		return serverEntranceService.getFcsMap(serverId);
	}
}
