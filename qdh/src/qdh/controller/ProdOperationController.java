package qdh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import qdh.pageModel.DataGrid;
import qdh.pageModel.Json;
import qdh.service.ProdOperationService;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/prodOptController")
public class ProdOperationController {
	
	@Autowired
	private ProdOperationService prodOperationService;
	
	@RequestMapping("/HQProdMgmt")
	public String HQProdMgmt() {
		
		return"/jsp/hq/hqAdmin/ProdMgmt.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/GetCurrentBrands")
	public DataGrid GetCurrentBrands(){
		DataGrid dataGrid = prodOperationService.getCurrentBrands();
		return dataGrid;
	}
}
