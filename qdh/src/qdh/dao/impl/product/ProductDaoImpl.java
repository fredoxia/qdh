package qdh.dao.impl.product;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import qdh.dao.entity.product.Product;
import qdh.dao.impl.BaseDAO;


@Repository
public class ProductDaoImpl extends BaseDAO<Product> {
	
	/**
	 * 获取Product表中的 distinct category
	 * @return
	 */
	public List<Integer> getDistinctCategory(){
		List<Integer> categories = new ArrayList<>();
		
		String hql = "SELECT DISTINCT category.category_ID FROM Product";
		
		List<Object> result = this.executeHQLSelect(hql, null, null, false);
		for (Object object : result){
			categories.add((Integer)object);
		}
		
		return categories;
	}
	
}
