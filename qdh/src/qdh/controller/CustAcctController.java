package qdh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import qdh.dao.entity.order.Customer;
import qdh.dao.impl.Response;
import qdh.pageModel.DataGrid;
import qdh.pageModel.Json;
import qdh.pageModel.SessionInfo;
import qdh.service.CustAcctService;

@Controller
@RequestMapping("/custAcctController")
public class CustAcctController {
	
	@Autowired
	private CustAcctService custAcctService;
	
	@RequestMapping("/CustAcctMgmt")
	public String CustAcctMgmt() {
		
		return "/jsp/hq/hqAdmin/CustAcctMgmt.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/GetCustAccts")
	public DataGrid GetCustAccts(Boolean isChain, String namePY){
		DataGrid dataGrid = custAcctService.getCustAccts(isChain, namePY);

		return dataGrid;
	}
	
	@RequestMapping("/PreAddUpdateCustAcct")
	public ModelAndView PreAddUpdateCustAcct(Customer cust){
		Response response = new Response();
		
		try {
			    response = custAcctService.prepareAddEditCustAcct(cust);
			} catch (Exception e){
				e.printStackTrace();
				response.setFail(e.getMessage());
			}

		ModelAndView mav = new ModelAndView();
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		mav.setViewName("/jsp/hq/hqAdmin/CustAcctAddUpdate.jsp");
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/AddUpdateCustAcct")
	public Json AddUpdateCustAcct(Customer cust, HttpSession session){
		Response response = new Response();
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		try {
			response = custAcctService.addUpdateAcct(cust,loginUser.getUserName());
		} catch (Exception e){
			e.printStackTrace();
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/DeleteCustAcct")
	public Json DeleteCustAcct(Integer id, HttpSession session){
		Response response = new Response();
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		try {
			response = custAcctService.deleteCustAcct(id,loginUser.getUserName());
		} catch (Exception e){
			e.printStackTrace();
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
}
