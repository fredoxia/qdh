package qdh.dao.impl.product;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.product.ProductUnit;
import qdh.dao.impl.BaseDAO;


@Repository
public class ProductUnitDaoImpl extends BaseDAO<ProductUnit>{

	public boolean checkExist(String unit) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductUnit.class);
		criteria.add(Restrictions.eq("productUnit", unit));
		
		List<ProductUnit> units = this.getByCritera(criteria, true);
		
		if (units == null || units.size() != 1)
			return false;
		else 
			return true;
	}
//
//	public ProductUnit getUnitByName(String productUnitString) {
//		DetachedCriteria criteria = DetachedCriteria.forClass(ProductUnit.class);
//		criteria.add(Restrictions.eq("productUnit", productUnitString));
//		
//		List<ProductUnit> units = this.getByCritera(criteria, true);
//		
//		if (units == null || units.size() != 1)
//			return null;
//		else 
//			return units.get(0);
//	}
}
