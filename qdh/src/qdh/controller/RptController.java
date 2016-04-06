package qdh.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import qdh.dao.entity.VO.FactoryOrderExcelVO;
import qdh.dao.impl.Response;
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
	public ModelAndView HQProdRpt(){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = rptService.preGenHQProdRpt();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqRpt/HQProdRpt.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}

	@ResponseBody
	@RequestMapping("/GenerateHQProdRpt")
	public DataGrid GenerateHQProdRpt(Integer cbId, Integer page, Integer rows, String sort, String order){
		DataGrid dataGrid = rptService.generateHQProdRpt(cbId, page, rows , sort, order);
		return dataGrid;
	}

	/**
	 * 当前订货中的客户订单排名情况
	 * @return
	 */
	@RequestMapping("/HQCustRpt")
	public String HQCustRpt(){
		
		return "/jsp/hq/hqRpt/HQCustRpt.jsp";
	}

	@ResponseBody
	@RequestMapping("/GenerateHQCustRpt")
	public DataGrid GenerateHQCustRpt(Integer page, Integer rows, String sort, String order){
		DataGrid dataGrid = rptService.generateHQCustRpt(page, rows , sort, order);
		return dataGrid;
	}
	
	@RequestMapping("/HQExportBrandOrder")
	public ModelAndView HQExportBrandOrder() {
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = rptService.preFactoryExportPage();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqRpt/HQExportBrandOrder.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	@RequestMapping("/HQExportFactoryOrder")
	public ModelAndView HQExportFactoryOrder(Integer cbId){
		
		Response response = new Response();
		try {
			response = rptService.getCustOrderProducts(cbId);
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		ModelAndView mav = new ModelAndView(new FactoryOrderExcelVO(), (Map<String, ?>)response.getReturnValue()); 
		
		return mav;
	}
	
	
	@RequestMapping("/GenerateProdRpt/mobile")
	public ModelAndView GenerateMobileProdRpt(Integer cbId){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = rptService.generateMobileProdRpt(cbId);
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/chainOrder/BarcodeRank.jsp");
		mav.addObject("barcodeRank", response.getReturnValue());
		
		return mav;
	}
	
}
