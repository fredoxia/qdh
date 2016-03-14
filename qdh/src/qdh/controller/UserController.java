package qdh.controller;


import javax.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qdh.dao.entity.qxMIS.UserInfor;
import qdh.dao.impl.Response;
import qdh.pageModel.Json;
import qdh.pageModel.SessionInfo;
import qdh.service.UserService;


@Controller
@RequestMapping("/userController")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/HQLogin")
	public Json HQLogin(UserInfor user, HttpSession session) {
		Json j = new Json();
		Response response = userService.HQlogin(user);
		if (response.isSuccess()) {
			j.setSuccess(true);
			j.setMsg("登陆成功！");

			SessionInfo sessionInfo = new SessionInfo();
			BeanUtils.copyProperties((UserInfor)response.getReturnValue(), sessionInfo);
			//sessionInfo.setIp(IpUtil.getIpAddr(request));
			//sessionInfo.setResourceList(userService.resourceList(u.getId()));
			
			session.setAttribute(ControllerConfig.HQ_SESSION_INFO, sessionInfo);

			j.setObj(sessionInfo);
		} else {
			j.setMsg(response.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/HQMain")
	public String HQMain() {
		
		return"/jsp/hq/Main.jsp";
	}
	
	@RequestMapping("/HQLogoff")
	public String HQLogoff(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return"/index.jsp";
	}

}
