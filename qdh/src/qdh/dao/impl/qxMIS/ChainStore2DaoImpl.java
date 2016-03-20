package qdh.dao.impl.qxMIS;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qdh.dao.entity.qxMIS.ChainStore2;
import qdh.dao.impl.BaseDAO2;


@Repository
public class ChainStore2DaoImpl extends BaseDAO2<ChainStore2>{
	public List<ChainStore2> getActiveChainStores(){
		DetachedCriteria criteria = DetachedCriteria.forClass(ChainStore2.class);
			criteria.add(Restrictions.ne("status", ChainStore2.STATUS_DISABLED));
		
		return this.getByCritera(criteria, true);
	}
}
