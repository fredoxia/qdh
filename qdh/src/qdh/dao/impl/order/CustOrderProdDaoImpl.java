package qdh.dao.impl.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
		criteria.add(Restrictions.eq("custId", userId));
		criteria.add(Restrictions.eq("productBarcode.id", pbId));
		List<Object> totalObj = this.getByCriteriaProjection(criteria, true);
		int q = NumUtility.getProjectionIntegerValue(totalObj);
		return q;
	}

	public int getTotalQ(Integer pbId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.setProjection(Projections.sum("quantity"));
		criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
		criteria.add(Restrictions.eq("productBarcode.id", pbId));
		List<Object> totalObj = this.getByCriteriaProjection(criteria, true);
		int q = NumUtility.getProjectionIntegerValue(totalObj);
		return q;
	}
	
	public List<Object> getMyTotal(Integer custId, Set<Integer> ids) {
		List<Object> myTotal = new ArrayList<>();
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.setProjection(Projections.projectionList().add(Projections.sum("quantity")).add(Projections.sum("sumWholePrice")));
		criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
		criteria.add(Restrictions.eq("custId", custId));
		if (ids != null)
			criteria.add(Restrictions.in("productBarcode.id", ids));
		List<Object> resultObj = this.getByCriteriaProjection(criteria, true);
		Object[] totalObj = null;
		if (resultObj == null || resultObj.size()<1){
			myTotal.add(0);
			myTotal.add(0);
		} else 
			totalObj = (Object[])resultObj.get(0);
		
		for (Object object : totalObj){
			if (object != null)
				myTotal.add(object);
			else 
				myTotal.add(0);
		}
		return myTotal;
	}


}
