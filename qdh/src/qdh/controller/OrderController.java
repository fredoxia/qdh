package qdh.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import qdh.dao.impl.Response;
import qdh.pageModel.Json;
import qdh.pageModel.SessionInfo;
import qdh.service.OrderService;

@Controller
@RequestMapping("/orderController")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@ResponseBody
	@RequestMapping("/OrderMore/mobile")
	public Json OrderMore(Integer pbId, Integer quantity, HttpSession session){
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		Response response = new Response();
		try {
			response = orderService.orderMore(loginUser, pbId, quantity);
		} catch (Exception e){
			response.setFail(e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	
	@RequestMapping("/StartOrder/mobile")
	public String StartOrder(HttpSession session){
		return "/jsp/chainOrder/StartOrder.jsp";
	}
}
