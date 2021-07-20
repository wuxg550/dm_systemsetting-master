package com.hy.zookeeper.config.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.hy.zookeeper.config.entity.ServerRelation;
import com.hy.zookeeper.config.enums.ResultKeyConst;
import com.hy.zookeeper.config.service.IServerRelationService;

@Controller
@RequestMapping("/relation")
public class RelationController {

	@Resource
	private IServerRelationService relationService;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
		 return new ModelAndView("relation/index");
	}
	
	@RequestMapping("/import")
	public ModelAndView importFile(HttpServletRequest request, HttpServletResponse response){
		 return new ModelAndView("relation/import");
	}
	
	@RequestMapping("/tree")
	@ResponseBody
	public Map<String, Object> getTree(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="15") int rows, ServerRelation relation){
		return relationService.getServerRelationTree(page, rows, relation);
	}
	
	@RequestMapping("/allRelation")
	@ResponseBody
	public Map<String, Object> getAllRelation(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="15") int rows, ServerRelation relation){
		return relationService.getRelationPage(page, rows, relation);
	}
	
	@RequestMapping("/editPage")
	public ModelAndView index(String id){
		 ModelAndView mv = new ModelAndView("relation/relationEdit");
		 if(id != null){
			ServerRelation r =  relationService.getRelationById(id);
			// 不返回敏感信息到前端
			r.setUserName("");
			r.setPassword("");
			r.setDestIp("");
			mv.addObject("relation", r);
		 }
		 return mv;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> saveRelation(ServerRelation relation){
		Map<String, Object> result = new HashMap<>();
		boolean success = relationService.saveRelation(relation);
		result.put(ResultKeyConst.SUCESS_KEY, success);
		return result;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> deleteRelation(String id){
		Map<String, Object> result = new HashMap<>();
		boolean success = relationService.deleteRelation(id);
		result.put(ResultKeyConst.SUCESS_KEY, success);
		return result;
	}
	
	@RequestMapping("/delByServerId")
	@ResponseBody
	public Map<String, Object> deleteRelationByServerId(String serverId){
		Map<String, Object> result = new HashMap<>();
		boolean success = relationService.deleteRelationByServerId(serverId);
		result.put(ResultKeyConst.SUCESS_KEY, success);
		return result;
	}
	
	@RequestMapping("/autoConfig")
	@ResponseBody
	public Map<String, Object> autoConfig(String serverTypes, String serverIds){
		Map<String, Object> result = new HashMap<>();
		List<String> typeList = JSON.parseArray(serverTypes, String.class);
		List<String> idList = JSON.parseArray(serverIds, String.class);
		boolean success = relationService.configRelationByTemple(typeList, idList);
		result.put(ResultKeyConst.SUCESS_KEY, success);
		return result;
	}
	
	@RequestMapping("/imposedConfig")
	@ResponseBody
	public Map<String, Object> imposedConfig(String serverTypes, String serverIds){
		Map<String, Object> result = new HashMap<>();
		List<String> typeList = JSON.parseArray(serverTypes, String.class);
		List<String> idList = JSON.parseArray(serverIds, String.class);
		boolean success = relationService.imposedConfingRelation(typeList, idList);
		result.put(ResultKeyConst.SUCESS_KEY, success);
		return result;
	}
	
	@RequestMapping("/validate")
	@ResponseBody
	public Map<String, Object> validateRelation(){
		Map<String, Object> result = new HashMap<>();
		try{
			relationService.validateRelation();
			result.put(ResultKeyConst.SUCESS_KEY, true);
		}catch(Exception e){
			result.put(ResultKeyConst.SUCESS_KEY, false);
		}
		return result;
	}
	
	@RequestMapping("/exportRelation")
	public void exportRelation(HttpServletResponse response){
		relationService.exportRelation(response);
	}
	
	
	/**    
     * 文件上传具体实现方法;    
     *     
     * @param file    
     * @return    
     */      
    @RequestMapping("/upload")      
    @ResponseBody      
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {      
    	return relationService.importRelation(file);  
    } 
	
    @RequestMapping("/relationMap")      
    @ResponseBody      
    public ModelAndView relationMap() {      
		return new ModelAndView("relation/relationMap");
    }
    
    @RequestMapping("/mapData")
	@ResponseBody
	public Map<String, Object> mapData(HttpServletRequest request, String serverIds){
		List<String> serverIdList = JSON.parseArray(serverIds, String.class);
		return relationService.getRelationMap(serverIdList);
	}
    
    @RequestMapping("/deleteLine")
	@ResponseBody
	public Map<String, Object> deleteLine(HttpServletRequest request, String lineId){
		return relationService.deleteRelationByLine(lineId);
	}
    
    @RequestMapping("/addLine")
	@ResponseBody
	public Map<String, Object> addLine(HttpServletRequest request, String srcNode, String destNode, String serverIds){
		return relationService.addRelationLine(srcNode, destNode, serverIds);
	}
    
    @RequestMapping("/batchDelete")
	@ResponseBody
	public Map<String, Object> batchDelete(String ids){
		return relationService.deleteRelationByIds(ids);
	}
    
    @RequestMapping("/generate")
    @ResponseBody
    public Object generateRelation(@RequestBody List<String> destServerType) {
    	return relationService.generateRelationByServerType(destServerType);
    }
}
