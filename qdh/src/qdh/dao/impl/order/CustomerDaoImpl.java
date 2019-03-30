package qdh.dao.impl.order;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qdh.dao.entity.order.Customer;
import qdh.dao.impl.BaseDAO;
import qdh.dao.impl.Response;
import qdh.utility.DateUtility;

@Repository
public class CustomerDaoImpl extends BaseDAO<Customer>{
	public void save(Customer cust, String user){
		cust.setUpdateUser(user);
		cust.setUpdateDate(DateUtility.getToday());
		this.save(cust, true);
	}
	
	public void update(Customer cust, String user){
		cust.setUpdateUser(user);
		cust.setUpdateDate(DateUtility.getToday());
		this.update(cust, true);
	}
}
