package qdh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qdh.dao.entity.qxMIS.UserInfor;
import qdh.dao.impl.Response;
import qdh.dao.impl.qxMIS.UserInforDaoImpl;

@Service
public class UserService {

	@Autowired
	private UserInforDaoImpl userInforDaoImpl;
	
	public Response HQlogin(UserInfor user) {
		
		return userInforDaoImpl.getUserByUserNamePwd(user.getUserName(), user.getPassword());
	}

	
}
