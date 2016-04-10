package qdh.dao.impl.order;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qdh.dao.config.EntityConfig;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.impl.BaseDAO;
import qdh.utility.NumUtility;

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

	public int getMyQ(int userId, Integer pbId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.setProjection(Projections.sum("quantity"));
		criteria.add(Restrictions.ne("status", EntityConfig.DELETED));
		criteria.add(Restrictions.eq("custId", userId));
		criteria.add(Restrictions.eq("productBarcode.id", pbId));
		List<Object> totalObj = this.getByCriteriaProjection(criteria, true);
		int q = NumUtility.getProjectionIntegerValue(totalObj);
		return q;
	}

	public int getTotalQ(Integer pbId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.setProjection(Projections.sum("quantity"));
		criteria.add(Restrictions.ne("status", EntityConfig.DELETED));
		criteria.add(Restrictions.eq("productBarcode.id", pbId));
		List<Object> totalObj = this.getByCriteriaProjection(criteria, true);
		int q = NumUtility.getProjectionIntegerValue(totalObj);
		return q;
	}
}
