package qdh.dao.impl.qxMIS;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qdh.dao.entity.qxMIS.ChainStore2;
import qdh.dao.impl.BaseDAO2;


@Repository
public class ChainStore2DaoImpl extends BaseDAO2<ChainStore2>{
	/**
	 * to get the chainStore information by client Id, it is used in the headQ part
	 * @param clientId
	 * @return
	 */
	public ChainStore2 getByClientId(int clientId){
		DetachedCriteria criteria = DetachedCriteria.forClass(ChainStore2.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		
		List<ChainStore2> chainStores = getByCritera(criteria, true);
		if (chainStores != null && chainStores.size() > 0)
			return chainStores.get(0);
		else {
			return null;
		}
	}
	
	public List<ChainStore2> getActiveChainStores(){
		DetachedCriteria criteria = DetachedCriteria.forClass(ChainStore2.class);
			criteria.add(Restrictions.ne("status", ChainStore2.STATUS_DISABLED));
		
		return this.getByCritera(criteria, true);
	}
}
