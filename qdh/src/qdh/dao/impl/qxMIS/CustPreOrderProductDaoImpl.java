package qdh.dao.impl.qxMIS;

import qdh.dao.entity.qxMIS.CustPreOrderProduct;
import qdh.dao.impl.BaseDAO2;

public class CustPreOrderProductDaoImpl  extends BaseDAO2<CustPreOrderProduct> {

	public void deleteByOrderId(int custPreOrderId) {
		String hql = "DELETE FROM CustPreOrderProduct WHERE preorderId = ?";
		Object[] values = new Object[]{custPreOrderId};
		
		this.executeHQLUpdateDelete(hql , values, false);
		
	}

}
