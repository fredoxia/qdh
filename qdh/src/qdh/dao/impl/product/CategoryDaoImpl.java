package qdh.dao.impl.product;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.product.Area;
import qdh.dao.entity.product.Category;
import qdh.dao.impl.BaseDAO;


@Repository
public class CategoryDaoImpl extends BaseDAO<Category>{
	public boolean checkCategoryExist(String category){
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq("category_Name", category));

		List<Category> categories = this.getByCritera(criteria, true);
		
		if (categories == null || categories.size() != 1)
			return false;
		else 
			return true;
	}
	
	public Category getCategoryByName(String categoryString) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq("category_Name", categoryString));

		List<Category> categories = this.getByCritera(criteria, true);
		
		return categories.get(0);
	}
}
