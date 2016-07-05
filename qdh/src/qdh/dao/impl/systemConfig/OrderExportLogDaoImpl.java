package qdh.dao.impl.systemConfig;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.systemConfig.OrderExportLog;
import qdh.dao.impl.BaseDAO;

@Repository
public class OrderExportLogDaoImpl extends BaseDAO<OrderExportLog>{

	public boolean hasExportHistory(String newOrderIdentity) {
		Object[] values = new Object[]{newOrderIdentity};
		String hql = "SELECT COUNT(id) FROM OrderExportLog WHERE orderIdentity =?";
		int result = this.executeHQLCount(hql, values, false);
		
		if (result > 0)
		    return true;
		else 
			return false;
	}
	
	
}
