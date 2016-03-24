package qdh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import qdh.pageModel.DataGrid;
import qdh.service.RptService;

@Controller
@RequestMapping("/rptController")
public class RptController {
	
	@Autowired
	private RptService rptService;
	
	/**
	 * 当前订货中的排名情况
	 * @return
	 */
	@RequestMapping("/HQProdRpt")
	public String HQProdRpt(){
		return "/jsp/hq/hqRpt/HQProdRpt.jsp";
	}

	@ResponseBody
	@RequestMapping("/GenerateHQProdRpt")
	public DataGrid GenerateHQProdRpt(Integer page, Integer rows, String sort, String order){
		DataGrid dataGrid = rptService.generateHQProdRpt(page, rows , sort, order);
		return dataGrid;
	}
}
