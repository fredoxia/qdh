package qdh.dao.impl.product;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
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
	
	public Product getBySerialNum(String serialNum) {
		DetachedCriteria productCriteria = DetachedCriteria.forClass(Product.class,"p");
		productCriteria.add(Restrictions.eq("p.serialNum", serialNum));

		List<Product> productList =  this.getByCritera(productCriteria, true);
		if (productList != null && productList.size() ==1){
		   Product product = productList.get(0);
		   return product;
		}else 
		   return null;
	}

	public List<Product> getProductsByProductCode(String productCode) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Product.class,"product");
		criteria.add(Restrictions.eq("product.productCode", productCode));

		List<Product> products =  this.getByCritera(criteria,true);
		
		return products;
	}
	
	public void initialize(Product product){
		initialize(product.getArea());
		initialize(product.getBrand());
		initialize(product.getCategory());
		initialize(product.getQuarter());
		initialize(product.getYear());
	}
	
	public void initialize(Collection<Product> products){
		for (Product product: products){
			initialize(product);
		}
	}

	
}
