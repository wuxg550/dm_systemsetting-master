package com.hy.zookeeper.config.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hy.zookeeper.config.common.ZkNode;
import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.enums.ResultKeyConst;
import com.hy.zookeeper.config.service.INodeDataService;

/**
 * NodeData controller
 * @author lcn
 * @email 3020690146@qq.com
 * @date 2018/01/18 10:43
 */
@Controller
@RequestMapping("/node")
public class NodeDataController {
	
	private static final Logger logger = LoggerFactory.getLogger(NodeDataController.class);

	@Autowired
	private INodeDataService nodeDataSerice;
	
	@Value("${zookeeper.address}")
	private String zkConnectString;
	
	@RequestMapping(value="/nodeList",method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView("nodedata/nodeList");
	}
	
	
	@RequestMapping(value="/findAllZkServer")
	@ResponseBody
	public List<Zkserver> findAllZkServer(){
		return nodeDataSerice.findAllZkServer();
	}
	
	/**
	 * 获取节点列表数据
	 * @param node
	 * @return
	 */
	@RequestMapping(value="/getNodeDataList")
	@ResponseBody
	public Map<String, Object> getNodeDataList(String node,String host){
		Map<String, Object> result = new HashMap<>();
		List<ZkNode> tableList =  nodeDataSerice.getNodeDataList(node,host);
		result.put("rows", tableList);
		result.put("total",1);
		result.put("records", tableList.size());
		return result;
	}
	
	@RequestMapping(value="/validate")
	@ResponseBody
	public Map<String, Object> validateNodeData(){
		Map<String, Object> result = new HashMap<>();
		try{
			nodeDataSerice.syncNodeData();
			result.put(ResultKeyConst.SUCESS_KEY, true);
		}catch(Exception e){
			logger.error("同步节点数据异常", e);
			result.put(ResultKeyConst.SUCESS_KEY, false);
		}
		return result;
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public Map<String, Object> deleteNode(String path){
		Map<String, Object> result = new HashMap<>();
		try{
			nodeDataSerice.deleteNode(path);
			result.put(ResultKeyConst.SUCESS_KEY, true);
		}catch(Exception e){
			result.put(ResultKeyConst.SUCESS_KEY, false);
		}
		return result;
	}
}
