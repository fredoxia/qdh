package qdh.dao.impl.order;



import java.util.List;

import org.apache.tools.ant.types.resources.Restrict;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.impl.BaseDAO;

@Repository
public class CurrentBrandsDaoImpl extends BaseDAO<CurrentBrands>{

	public CurrentBrands getByKey(int yearId, int quarterId, int brandId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CurrentBrands.class);
		criteria.add(Restrictions.eq("year.year_ID", yearId));
		criteria.add(Restrictions.eq("quarter.quarter_ID", quarterId));
		criteria.add(Restrictions.eq("brand.brand_ID", brandId));
		List<CurrentBrands> currentBrands = getByCritera(criteria, true);
		if (currentBrands.size() > 0)
			return currentBrands.get(0);
		else 
			return null;
	}


}
