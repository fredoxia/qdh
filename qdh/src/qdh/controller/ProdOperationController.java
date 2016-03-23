package qdh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import qdh.dao.entity.qxMIS.UserInfor2;
import qdh.dao.impl.Response;
import qdh.pageModel.DataGrid;
import qdh.pageModel.Json;
import qdh.pageModel.SessionInfo;
import qdh.service.ProdOperationService;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/prodOptController")
public class ProdOperationController {
	
	@Autowired
	private ProdOperationService prodOperationService;
	
	@RequestMapping("/HQProdMgmt")
	public String HQProdMgmt() {
		
		return "/jsp/hq/hqAdmin/ProdMgmt.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/GetCurrentBrands")
	public DataGrid GetCurrentBrands(){
		DataGrid dataGrid = prodOperationService.getCurrentBrands();
		return dataGrid;
	}
	
	@ResponseBody
	@RequestMapping("/DeleteCurrentBrand")
	public Json DeleteCurrentBrand(int currentBrandId){
		Response response = new Response();
		try {
			response = prodOperationService.deleteCurrentBrand(currentBrandId);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	
	@RequestMapping("/PreAddCurrentBrand")
	public ModelAndView PreAddCurrentBrand(){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.preAddCurrentBrand();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmtAdd.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/AddCurrentBrand")
	public Json AddCurrentBrand(int yearId, int quarterId, int brandId, HttpSession session){
		Response response = new Response();
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		try {
			response = prodOperationService.addCurrentBrand(yearId, quarterId, brandId,loginUser.getUserName());
		} catch (Exception e){
			e.printStackTrace();
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@RequestMapping("/HQBarcodeMgmt")
	public ModelAndView HQBarcodeMgmt() {
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.preBarcodeSearch();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/BarcodeMgmt.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/GetBarcodes")
	public DataGrid GetBarcodes(Integer cbId, String sort, String order, Integer page, Integer rows){
		DataGrid dataGrid = prodOperationService.getBarcodes(cbId, sort,order,page, rows);
		return dataGrid;
	}
	
}
