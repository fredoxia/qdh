package qdh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode;

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
	public DataGrid GetCustAccts(Integer custType, String custName, String sort, String order){
		DataGrid dataGrid = custAcctService.getCustAccts(custType, custName, sort, order);

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
	
	@ResponseBody
	@RequestMapping("/CheckCustOrder")
	public Json CheckCustOrder(Integer id, HttpSession session){
		Response response = new Response();
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		try {
			response = custAcctService.checkCustOrder(id);
		} catch (Exception e){
			e.printStackTrace();
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@RequestMapping("/OpenCustOrderJSP")
	public ModelAndView GetCustOrderJSP(Integer id, HttpSession session){
		ModelAndView maView = new ModelAndView();
		maView.setViewName("/jsp/hq/hqAdmin/CustOrder.jsp");
		maView.addObject("custId", id);
		return maView;
	}
	
	@ResponseBody
	@RequestMapping("/GetCustOrderData")
	public DataGrid GetCustOrderData(Integer id, HttpSession session){
		DataGrid dataGrid = new DataGrid();
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		
		dataGrid = custAcctService.getCustOrder(id);


		return dataGrid;
	}
	
}
