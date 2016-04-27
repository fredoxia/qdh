package qdh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import qdh.dao.entity.VO.FactoryOrderExcelVO;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.impl.Response;
import qdh.pageModel.DataGrid;
import qdh.pageModel.SessionInfo;
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
	
	@RequestMapping("/HQOrderExportLog")
	public String HQOrderExportLog(){
		
		return "/jsp/hq/hqAdmin/CustOrderExport.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/GetHQOrderExportLog")
	public DataGrid GetHQOrderExportLog(){
		DataGrid dataGrid = rptService.getHQOrderExportLog();
		return dataGrid;
	}
	
	/**
	 * 所有连锁店订货排名情况
	 * @param cb
	 * @param session
	 * @return
	 */
	@RequestMapping("/GenerateProdRpt/mobile")
	public ModelAndView GenerateMobileProdRpt(CurrentBrands cb, HttpSession session){
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
	
			response = rptService.generateMobileProdRpt(cb,loginUser.getUserId());
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/chainOrder/BarcodeRank.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	/**
	 * 连锁店客户自己的订货情况
	 * @param cb
	 * @param session
	 * @return
	 */
	@RequestMapping("/CustRpt/mobile")
	public ModelAndView CustRpt(CurrentBrands cb, HttpSession session){
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
	
			response = rptService.generateMobileCustRpt(loginUser.getUserId(),cb);
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/chainOrder/MyOrder.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
//	@ResponseBody
//	@RequestMapping("/GenerateProdRptData/mobile")
//	public DataGrid GenerateMobileCustRptData(CurrentBrands cb, Integer page, Integer rows, String sort, String order, HttpSession session){
//		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
//	
//		Response response = new Response();
//		DataGrid dataGrid = new DataGrid();
//		
//		try {
//	
//			response = rptService.generateMobileCustRpt(loginUser.getUserId(),cb, page, rows, sort, order);
//			dataGrid = (DataGrid)response.getReturnValue();
//		} catch (Exception e){
//			response.setFail("系统错误 : " + e.getMessage());
//		}
//		
//
//		return dataGrid;
//	}
	
}
