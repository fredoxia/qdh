package qdh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qdh.dao.config.EntityConfig;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.qxMIS.UserInfor2;
import qdh.dao.entity.systemConfig.SystemConfig;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.qxMIS.UserInfor2DaoImpl;
import qdh.dao.impl.systemConfig.SystemConfigDaoImpl;

@Service
public class UserService {

	@Autowired
	private UserInfor2DaoImpl userInforDaoImpl;
	
	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;
	
	public Response HQlogin(UserInfor2 user) {
		if (user.getUserName().equals("admin")){
			return userInforDaoImpl.getLocalAdmin(user.getUserName(), user.getPassword());
		} else 
			return userInforDaoImpl.getUserByUserNamePwd(user.getUserName(), user.getPassword());
	}

	public Response loginMobile(Customer cust) {
		Response response = new Response();
		if (cust.getId() == 0)
			response.setFail("用户名不存在");
		else {
			/**
			 * 1. 如果还没有SystemConfi数据或者订货会代码是空，提示错误
			 */
			SystemConfig systemConfig = systemConfigDaoImpl.getSystemConfig();
			if (systemConfig == null || systemConfig.getOrderIdentity().trim().equals("")){
				response.setFail("订货会信息尚未配置好，请联系管理员");
				return response;
			}
			
			Customer cust2 = customerDaoImpl.get(cust.getId(), false);
			
			if (cust2 == null)
				response.setFail("用户名不存在");
			else if (!cust.getPassword().equals(cust2.getPassword()))
				response.setFail("登录密码错误");
			else if (cust2.getStatus() != EntityConfig.ACTIVE)
				response.setFail("账户状态 锁定。请通知总部员工");
			else
				response.setReturnValue(cust2);
		}
		return response;
	}

	
}
