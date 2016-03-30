package qdh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qdh.dao.entity.order.Customer;
import qdh.dao.entity.qxMIS.UserInfor2;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.qxMIS.UserInfor2DaoImpl;

@Service
public class UserService {

	@Autowired
	private UserInfor2DaoImpl userInforDaoImpl;
	
	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
	public Response HQlogin(UserInfor2 user) {
		
		return userInforDaoImpl.getUserByUserNamePwd(user.getUserName(), user.getPassword());
	}

	public Response loginMobile(Customer cust) {
		
		return customerDaoImpl.getCustByUserNamePwd(cust.getId(), cust.getPassword());
	}

	
}
