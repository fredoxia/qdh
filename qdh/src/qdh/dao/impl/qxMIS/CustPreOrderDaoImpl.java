package qdh.dao.impl.qxMIS;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import qdh.dao.entity.qxMIS.CustPreOrder;
import qdh.dao.impl.BaseDAO2;

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

}
