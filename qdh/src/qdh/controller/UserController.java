package qdh.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import qdh.dao.entity.qxMIS.UserInfor;
import qdh.pageModel.Json;
import qdh.pageModel.SessionInfo;
import qdh.service.UserService;
import qdh.utility.IpUtil;

@Controller
@RequestMapping("/userController")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/HQlogin")
	public Json login(UserInfor user, HttpSession session, HttpServletRequest request) {
		Json j = new Json();
		UserInfor u = userService.HQlogin(user);
		if (u != null) {
			j.setSuccess(true);
			j.setMsg("登陆成功！");

			SessionInfo sessionInfo = new SessionInfo();
			//BeanUtils.copyProperties(u, sessionInfo);
			sessionInfo.setIp(IpUtil.getIpAddr(request));
			//sessionInfo.setResourceList(userService.resourceList(u.getId()));

			j.setObj(sessionInfo);
		} else {
			j.setMsg("用户名或密码错误！");
		}
		return j;
	}
	

}
