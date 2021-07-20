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

import com.hy.zookeeper.config.entity.PlatformServerType;
import com.hy.zookeeper.config.service.IServerTypeService;

@Controller
@RequestMapping("/serverType")
public class 	ServerTypeController {
	
	@Autowired
	private IServerTypeService iServerTypeService;
	
	
	@RequestMapping(value="/serverTypeList",method=RequestMethod.GET)
	public ModelAndView indexs(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView("server/serverType");
	}
	
	
	@RequestMapping(value="/updataServerType",method=RequestMethod.GET)
	public ModelAndView serverInfoUpdate(HttpServletRequest request,String id){
		ModelAndView mv = new ModelAndView("server/serverTypeEdit");
		if(id != null){
			PlatformServerType serverType = iServerTypeService.getTypeByID(id);
			mv.addObject("serverType",serverType);
		}
		return mv;
	}
	

	@RequestMapping(value="/serverTypeEdit",method=RequestMethod.GET)
	public ModelAndView configServerEdit(HttpServletRequest request,HttpServletResponse reponse){
		return new ModelAndView("server/serverTypeEdit");
	}
	
	
	@RequestMapping("/getAll")
	@ResponseBody
	public List<PlatformServerType> getAllType(HttpServletRequest request){
		return iServerTypeService.getAllType();
	}
	
	@RequestMapping("/getAllTypePage")
	@ResponseBody
	public Map<String,Object> getAllTypePage(HttpServletRequest request,int page,int rows, PlatformServerType serverType){
		return iServerTypeService.getAllTypePage(page,rows,serverType);
	}

	@RequestMapping("/getNoUseType")
	@ResponseBody
	public List<PlatformServerType> getNoUseType(HttpServletRequest request,String serverId){
		return iServerTypeService.getNoUseType(serverId);
	}
	
	@RequestMapping("/saveType")
	@ResponseBody
	public boolean savaServerType(PlatformServerType serverType){
		return iServerTypeService.saveServerType(serverType);
	}
	
	@RequestMapping("/deletType")
	@ResponseBody
	public boolean delServerType(String id){
		return iServerTypeService.deleteType(id);
	}

	@RequestMapping("/exist")
	@ResponseBody
	public boolean exist(PlatformServerType serverType){
		return (iServerTypeService.getCount(serverType) > 0);
	}
	
}
