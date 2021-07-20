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

import com.hy.zookeeper.config.entity.AgreementType;
import com.hy.zookeeper.config.service.IAgreementTypeService;

@Controller
@RequestMapping("/agreement")
public class AgreementTypeController {
	
	@Autowired
	private IAgreementTypeService agreementTypeService;
	
	@RequestMapping(value="/agreementList",method=RequestMethod.GET)
	public ModelAndView indexs(HttpServletRequest request, HttpServletResponse response) {
		 return new ModelAndView("server/agreementTypeList");
	}
	
	@RequestMapping(value="/editAgreementType",method=RequestMethod.GET)
	public ModelAndView serverInfoUpdate(HttpServletRequest request,String id){
		ModelAndView mv = new ModelAndView("server/agreementTypeEdit");
		if(!"".equals(id)){
			AgreementType agreementype = agreementTypeService.getById(id);
			mv.addObject("agreementType",agreementype);
		}
		return mv;
	}
	
	@RequestMapping("/getAllType")
	@ResponseBody
	public List<AgreementType> getAllType(){
		return agreementTypeService.getAll();
	}
	
	@RequestMapping("/getAllTypePage")
	@ResponseBody
	public Map<String,Object> getAllTypePage(HttpServletRequest request,int page,int rows){
		return agreementTypeService.findAllPage(page, rows);
	}
	
	@RequestMapping("/saveAgreement")
	@ResponseBody
	public boolean saveAgreementType(AgreementType agreementType){
		return agreementTypeService.save(agreementType);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public boolean delete(String id){
		return agreementTypeService.deleteById(id);
	}
}
