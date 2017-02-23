package qdh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import qdh.dao.entity.VO.CustomerOrderExcelVO;
import qdh.dao.entity.VO.FactoryOrderExcelVO;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.systemConfig.SystemConfig;
import qdh.dao.impl.Response;
import qdh.pageModel.DataGrid;
import qdh.pageModel.Json;
import qdh.pageModel.SessionInfo;
import qdh.service.CustAcctService;
import qdh.service.SystemConfigService;

@Controller
@RequestMapping("/systemConfigController")
public class SystemConfigController {
	
	@Autowired
	private SystemConfigService systemConfigService;
	
	@RequestMapping("/PreSystemConfig")
	public ModelAndView PreSystemConfig(){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = systemConfigService.PreSystemConfigPage();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/SystemConfig.jsp");
		mav.addObject("command", response.getReturnValue());
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateSystemConfig")
	public Json UpdateSystemConfig(SystemConfig systemConfig, HttpSession session){
		Response response = new Response();
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		try {
			response = systemConfigService.updateSystemConfig(systemConfig,loginUser.getUserName());
		} catch (Exception e){
			e.printStackTrace();
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/DeleteCurrentOrderData")
	public Json DeleteCurrentOrderData(){
		Response response = new Response();
		try {
			response = systemConfigService.deleteCurrentOrderData();
		} catch (Exception e){
			e.printStackTrace();
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
}
