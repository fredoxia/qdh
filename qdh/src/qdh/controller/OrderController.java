package qdh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import qdh.dao.entity.VO.AllCustOrderDownloadView;
import qdh.dao.entity.VO.CustomerOrderExcelVO;
import qdh.dao.impl.Response;
import qdh.pageModel.Json;
import qdh.pageModel.SessionInfo;
import qdh.service.OrderService;

@Controller
@RequestMapping("/orderController")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 在所有品牌排名上点击 加订
	 * @param pbId
	 * @param quantity
	 * @param session
	 * @return
	 */
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
	
	/**
	 * 在my order页面上 点击 加订 减订
	 * @param pbId
	 * @param quantity
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/MyOrderMore/mobile")
	public Json MyOrderMore(Integer pbId, Integer quantity, Integer cbId, HttpSession session){
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		Response response = new Response();
		try {
			response = orderService.myOrderMore(loginUser, pbId, cbId, quantity);
		} catch (Exception e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/MyOrderMoreByCategory/mobile")
	public Json MyOrderMoreByCategory(Integer pbId, Integer quantity, Integer categoryId, HttpSession session){
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		Response response = new Response();
		try {
			response = orderService.myOrderMoreByCategory(loginUser, pbId, categoryId, quantity);
		} catch (Exception e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	/**
	 * 在start order页面上 点击 加订 减订
	 * @param pbId
	 * @param quantity
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/StartOrderMore/mobile")
	public Json StartOrderMore(Integer pbId, Integer quantity, HttpSession session){
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		Response response = new Response();
		try {
			response = orderService.startOrderMore(loginUser, pbId, quantity);
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
	
	/**
	 * 客户查询产品
	 * @param productCode
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/SearchProduct/mobile")
	public Json SearchProduct(String productCode, HttpSession session){
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		Response response = new Response();
		try {
			response = orderService.searchProduct(loginUser.getUserId(), productCode);
		} catch (Exception e){
			response.setFail(e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	/**
	 * 导出订货会单据
	 * @param productCode
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ExportOrders")
	public Json ExportOrders(HttpSession session){
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		Response response = new Response();
		try {
			response = orderService.exportOrders(loginUser.getUserId());
		} catch (Exception e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	/**
	 * 总部导出所有当前订货会客户订单，分客户
	 * 打成一个zip文件
	 * @param session
	 * @return
	 */
	@RequestMapping("/HQDownloadAllCustOrder")
	public View HQDownloadAllCustOrder(HttpSession session){
		
		Response response = new Response();
		try {
			response = orderService.downalodAllCustOrder();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
	    return new AllCustOrderDownloadView((String)response.getReturnValue());
	}
	
	
	/**
	 * 总部导出所有当前订货会客户订单，分客户
	 * 打成一个zip文件
	 * @param session
	 * @return
	 */
	@RequestMapping("/HQDownloadAllCustOrderForHQOrdering")
	public View HQDownloadAllCustOrderForHQOrdering(HttpSession session){
		
		Response response = new Response();
		try {
			response = orderService.downalodAllCustOrderForHQ();
		} catch (Exception e){
			e.printStackTrace();
			response.setFail("系统错误 : " + e.getMessage());
		}
		
	    return new AllCustOrderDownloadView((String)response.getReturnValue(), "所有客户打印数量订单");
	}
}
