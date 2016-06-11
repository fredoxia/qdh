package qdh.dao.impl.qxMIS;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


import qdh.dao.entity.qxMIS.UserInfor2;
import qdh.dao.impl.BaseDAO2;
import qdh.dao.impl.Response;


@Repository
public class UserInfor2DaoImpl extends BaseDAO2<UserInfor2>{
	
	public Response getLocalAdmin(String userName, String pwd){
		Response response = new Response();
		if (pwd.equals("9876")){
			 UserInfor2 userInfor2 = new UserInfor2();
			 userInfor2.setUserName(userName);
			 userInfor2.setUserId(9999);
			 response.setReturnValue(userInfor2);
		} else {
			 response.setFail("管理员密码错误");
		}
		
		return response;
	}
	
	public Response getUserByUserNamePwd(String userName, String pwd){
		Response response = new Response();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(UserInfor2.class);
		criteria.add(Restrictions.eq("userName", userName));
		criteria.add(Restrictions.eq("password", pwd));
		
		List<UserInfor2> users = getByCritera(criteria, true);
		if (users == null || users.size() != 1)
			response.setFail("用户名不存在或者密码错误");
		else {
			response.setSuccess("");
			response.setReturnValue(users.get(0));
		}
		
		return response;
	}


}
