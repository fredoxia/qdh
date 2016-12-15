package qdh.dao.impl.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import qdh.dao.entity.order.ProductCategoryInSystem;
import qdh.dao.impl.BaseDAO;

@Repository
public class ProductCategoryInSystemDaoImpl  extends BaseDAO<ProductCategoryInSystem>{

	/**
	 * 清除所有
	 */
	public void deleteAll() {

		String hql = "DELETE FROM ProductCategoryInSystem";
		
		this.executeHQLUpdateDelete(hql, null, true);
		
	}

}
