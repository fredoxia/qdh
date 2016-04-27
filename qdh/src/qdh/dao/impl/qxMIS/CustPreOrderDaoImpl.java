package qdh.dao.impl.qxMIS;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.qxMIS.CustPreOrder;
import qdh.dao.impl.BaseDAO2;

@Repository
public class CustPreOrderDaoImpl  extends BaseDAO2<CustPreOrder> {

	public CustPreOrder getByCustIdOrderIdentity(Integer custId, String orderIdentity) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustPreOrder.class);
			criteria.add(Restrictions.eq("custId", custId));
			criteria.add(Restrictions.eq("orderIdentity", orderIdentity));
		List<CustPreOrder> custPreOrders = getByCritera(criteria, false);
		if (custPreOrders == null || custPreOrders.size() == 0)
		   return null;
		else 
		   return custPreOrders.get(0);
	}
	@Override
	public void delete(CustPreOrder entity, boolean cached){
		getHibernateTemplateMS().setCacheQueries(cached);
		
		this.getHibernateTemplateMS().delete(entity);
	}
	
	@Override
	public void save(CustPreOrder entity, boolean cached) {
		getHibernateTemplateMS().setCacheQueries(cached);
		
		this.getHibernateTemplateMS().save(entity);
	}

	@Override
	public void update(CustPreOrder entity, boolean cached) {
		getHibernateTemplateMS().setCacheQueries(cached);
		
		this.getHibernateTemplateMS().update(entity);
	}

}
