package qdh.dao.impl.qxMIS;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


import qdh.dao.entity.qxMIS.UserInfor;
import qdh.dao.impl.BaseDAO2;
import qdh.dao.impl.Response;


@Repository
public class UserInforDaoImpl extends BaseDAO2<UserInfor>{
	
	public Response getUserByUserNamePwd(String userName, String pwd){
		Response response = new Response();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(UserInfor.class);
		criteria.add(Restrictions.eq("userName", userName));
		criteria.add(Restrictions.eq("password", pwd));
		
		List<UserInfor> users = getByCritera(criteria, true);
		if (users == null || users.size() != 1)
			response.setFail("用户名不存在或者密码错误");
		else {
			response.setSuccess("");
			response.setReturnValue(users.get(0));
		}
		
		return response;
	}


}
