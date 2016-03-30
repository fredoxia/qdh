package qdh.dao.impl.order;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qdh.dao.entity.order.Customer;
import qdh.dao.impl.BaseDAO;
import qdh.dao.impl.Response;

@Repository
public class CustomerDaoImpl extends BaseDAO<Customer>{

	public Response getCustByUserNamePwd(int id, String password) {
		Response response = new Response();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("password", password));
		
		List<Customer> users = getByCritera(criteria, true);
		if (users == null || users.size() != 1)
			response.setFail("登陆失败");
		else {
			response.setSuccess("");
			response.setReturnValue(users.get(0));
		}
		
		return response;
	}

}
