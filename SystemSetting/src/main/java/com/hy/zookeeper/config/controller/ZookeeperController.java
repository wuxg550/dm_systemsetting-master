package com.hy.zookeeper.config.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.service.IZkserverService;

/**
 * zookeeper controller
 * @author lcn
 * @email 3020690146@qq.com
 * @date 2018/01/10 10:43
 */
@Controller
public class ZookeeperController {
	protected static final Logger logger = LoggerFactory.getLogger(ZookeeperController.class);
	
	private static final String SYSTEM_INDEX_PAGE = "system/index";
	
	@Autowired
	private IZkserverService zkserverService;
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView indexs(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView(SYSTEM_INDEX_PAGE);
	}
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView(SYSTEM_INDEX_PAGE);
	}
	
	@RequestMapping(value="/platformConfig",method=RequestMethod.GET)
	public ModelAndView platformConfig(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView(SYSTEM_INDEX_PAGE);
	}
	
	@RequestMapping(value="/serverList",method=RequestMethod.GET)
	public ModelAndView serverList(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView("server/serverList");
	}
	
	@RequestMapping(value="/serverEdit",method=RequestMethod.GET)
	public ModelAndView serverEdit(HttpServletRequest request, String id) {
		 ModelAndView mv = new ModelAndView("server/serverEdit");
		 if(id!=null){
			 Zkserver zkserver = zkserverService.findZkserverById(id);
			 mv.addObject("zkserver", zkserver);
		 }
		 return mv;
	}
	
	/**
	 * 服务列表查询
	 * @throws UnsupportedEncodingException 
	 * */
	@RequestMapping("/getAllPage")
	@ResponseBody
	public Map<String, Object> getAllPage(int page, int rows,Zkserver zkserver){
		Map<String, Object> result = new HashMap<>();
		List<Zkserver> tableList =  zkserverService.findByPage(zkserver, page, rows);
		result.put("rows", tableList);
		result.put("total",1);
		result.put("records", tableList.size());
		return result;
	}
	/**
	 * 保存服务
	 * @param zkserver
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/saveServer")
	@ResponseBody
	public boolean saveServer(Zkserver zkserver) {
		return zkserverService.saveServer(zkserver);
	}
	 
	/**
	 * 删除服务
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delServer/{id}",method=RequestMethod.GET)	
	@ResponseBody
	public boolean delServer(HttpServletRequest request,@PathVariable("id")String id) {		
		return zkserverService.delServer(id);
	}
	
	/**
	 * 初始化节点
	 * @param request
	 * @param host
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/initNode",method=RequestMethod.GET)	
	@ResponseBody
	public boolean initNode(HttpServletRequest request,String ip,String port){		
		return zkserverService.initNode(ip,port);
	}
}