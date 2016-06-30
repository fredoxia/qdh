package qdh.dao.impl.qxMIS;

import org.springframework.stereotype.Repository;

import qdh.dao.entity.qxMIS.CustPreorderIdentity;
import qdh.dao.impl.BaseDAO2;


@Repository
public class PreOrderIdentityDaoImpl extends BaseDAO2<CustPreorderIdentity> {
	
	@Override
	public void saveOrUpdate(CustPreorderIdentity entity, boolean cached) {
		if (cached)
			getHibernateTemplateMS().setCacheQueries(true);

		getHibernateTemplateMS().saveOrUpdate(entity);
	}
	
}
