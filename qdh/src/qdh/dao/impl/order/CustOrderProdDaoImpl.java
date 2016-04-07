package qdh.dao.impl.order;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.impl.BaseDAO;

@Repository
public class CustOrderProdDaoImpl extends BaseDAO<CustOrderProduct>{
	public CustOrderProduct getByPk(int custId, int pbId){
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.add(Restrictions.eq("custId", custId));
		criteria.add(Restrictions.eq("productBarcode.id", pbId));
		List<CustOrderProduct> products = this.getByCritera(criteria, true);
		if (products.size() == 1)
			return products.get(0);
		else
			return null;
	}
}
