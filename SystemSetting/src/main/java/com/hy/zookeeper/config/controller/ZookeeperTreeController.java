package com.hy.zookeeper.config.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hy.zookeeper.config.common.TreeNode;
import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.service.IZktreeService;

/**
 * zookeeper节点control
 * @author hrh
 *
 */
@Controller
@RequestMapping("/tree")
public class ZookeeperTreeController {
	protected static final Logger logger = LoggerFactory.getLogger(ZookeeperTreeController.class);
	
	@Autowired
	private IZktreeService zktreeService;
	
	@RequestMapping(value="/treeList",method=RequestMethod.GET)
	public ModelAndView treeList(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView("tree/tree");
	}
	
	/**
	 * 获取节点
	 * @param path
	 * @return 某个节点下的子节点
	 * @throws Exception 
	 */
	@RequestMapping(value="/getTree")
	@ResponseBody
	public List<TreeNode> getTree(String path,String ip,String port){
		return zktreeService.getZkTree(path, ip, port);
	}
	
	@RequestMapping(value="/getAllServer")
	@ResponseBody
	public List<Zkserver> getAllServer(){
		return zktreeService.finAllServer();
	}
	
	
		
}
