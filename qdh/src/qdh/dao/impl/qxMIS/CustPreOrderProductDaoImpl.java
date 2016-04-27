package qdh.dao.impl.qxMIS;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import qdh.dao.entity.qxMIS.CustPreOrderProduct;
import qdh.dao.impl.BaseDAO2;

@Repository
public class CustPreOrderProductDaoImpl  extends BaseDAO2<CustPreOrderProduct> {

	public void deleteByOrderId(int custPreOrderId) {
		String hql = "DELETE FROM CustPreOrderProduct WHERE preorderId = ?";
		Object[] values = new Object[]{custPreOrderId};
		
		this.executeHQLUpdateDelete(hql , values, false);
		
	}

	@Override
	public void save(CustPreOrderProduct entity, boolean cached) {
		getHibernateTemplateMS().setCacheQueries(cached);
		
		this.getHibernateTemplateMS().save(entity);
	}

	@Override
	public void update(CustPreOrderProduct entity, boolean cached) {
		getHibernateTemplateMS().setCacheQueries(cached);
		
		this.getHibernateTemplateMS().update(entity);
	}
	
	@Override	
	public int executeHQLUpdateDelete(final String queryString, final Object[] values, boolean cached){
		getHibernateTemplateMS().setCacheQueries(cached);
		
		return getHibernateTemplateMS().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
            	Query q= session.createQuery(queryString);
            	if (values != null && values.length > 0)
            	  for (int i =0; i < values.length; i++)
    				  q.setParameter(i, values[i]);
            	
    			int success = q.executeUpdate();
				return success;
			}
		});	
	}
}
