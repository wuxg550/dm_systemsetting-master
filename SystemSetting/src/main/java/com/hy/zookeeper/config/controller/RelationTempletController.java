package com.hy.zookeeper.config.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hy.zookeeper.config.entity.RelationTemplet;
import com.hy.zookeeper.config.service.IRelationTempletService;
import com.hy.zookeeper.config.util.FileUtils;

@Controller
@RequestMapping("/relation/templet")
public class RelationTempletController {

	private static final Logger logger = LoggerFactory.getLogger(RelationTempletController.class);
	
	@Resource
	private IRelationTempletService templetService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView indexs(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView("relation/templet/index");
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> getTempletList(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="15") int rows, RelationTemplet templet){
		return templetService.getTempletPage(page, rows, templet);
	}
	
	@RequestMapping(value="/editPage",method=RequestMethod.GET)
	public ModelAndView editPage(HttpServletRequest request, HttpServletResponse response, String id) {
		 ModelAndView mv = new ModelAndView("relation/templet/templetEdit");
		 if(StringUtils.isNotBlank(id)){
			 RelationTemplet templet = templetService.getTempletById(id);
			 mv.addObject("templet", templet);
		 }
		 return mv;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> saveTemplet(RelationTemplet templet){
		Map<String, Object> result = new HashMap<>();
		result.put("success", templetService.saveTemplet(templet));
		return result;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> deleteTemplet(String id){
		Map<String, Object> result = new HashMap<>();
		result.put("success", templetService.deleteTemplet(id));
		return result;
	}
	
	@RequestMapping("/checkExist")
	@ResponseBody
	public Map<String, Object> checkExist(RelationTemplet templet){
		Map<String, Object> result = new HashMap<>();
		result.put("exist", templetService.existTmeplet(templet));
		return result;
	}
	
	@RequestMapping("/uploadExcel")
	@ResponseBody
	public Map<String, Object> uploadExcel(HttpServletRequest request){
		MultipartHttpServletRequest multireq = (MultipartHttpServletRequest)request;
		List<MultipartFile> files = multireq.getFiles("file");
		return templetService.uploadExcelTemplet(files);
	}
	
	@RequestMapping("/downloadExcelTemplet")
	public void downloadExcelTemplet(HttpServletRequest request, HttpServletResponse response){
		String fileName = "流向模板.xls";
		//读取要下载的文件，保存到文件输入流
		String filePath = FileUtils.searchConfigFileAbsolutePath("/static/download/" + fileName);
		File file = new File(filePath);
		try (FileInputStream in = new FileInputStream(file);
				OutputStream out = response.getOutputStream()){
			
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			//创建缓冲区
			byte[] buffer = new byte[1024];
			int len = 0;
			//循环将输入流中的内容读取到缓冲区当中
			while((len=in.read(buffer))>0){
			    //输出缓冲区的内容到浏览器
			    out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			logger.error("导出Excel异常", e);
		}
	}
	
	/**
	 * 导入本地文件页面
	 */
	@RequestMapping(value="/uploadPage",method=RequestMethod.GET)
	public ModelAndView uploadPage(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("relation/templet/import");
	}
	
	@RequestMapping("/downloadExcel")
	@ResponseBody
	public void downloadExcel(HttpServletRequest request, HttpServletResponse response, RelationTemplet templet){
		templetService.downloadTempletExcel(templet, response);
	}
}
