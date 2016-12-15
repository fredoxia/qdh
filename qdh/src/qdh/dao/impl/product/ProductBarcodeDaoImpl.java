package qdh.dao.impl.product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.impl.BaseDAO;

@Repository
public class ProductBarcodeDaoImpl  extends BaseDAO<ProductBarcode> {

	public Set<Integer> getIds(int yearId, int quarterId, int brandId) {
		Set<Integer> idSet = new HashSet<Integer>();
		
		String ids = "SELECT pb.id FROM ProductBarcode pb JOIN pb.product p WHERE p.year.year_ID =? AND p.quarter.quarter_ID =? AND p.brand.brand_ID=?";

		Object[] values = new Object[]{yearId, quarterId, brandId};
		
		List<Object> idObjects = this.executeHQLSelect(ids, values, null, true);
		if (idObjects != null){
			for (Object obj : idObjects){
				idSet.add((Integer)obj);
			}
		}
		return idSet;
	}

	public Set<Integer> getIdsByCategoryId(int categoryId) {
		Set<Integer> idSet = new HashSet<Integer>();
		
		String ids = "SELECT pb.id FROM ProductBarcode pb JOIN pb.product p WHERE p.category.category_ID=?";

		Object[] values = new Object[]{categoryId};
		
		List<Object> idObjects = this.executeHQLSelect(ids, values, null, true);
		if (idObjects != null){
			for (Object obj : idObjects){
				idSet.add((Integer)obj);
			}
		}
		return idSet;
	}


}
