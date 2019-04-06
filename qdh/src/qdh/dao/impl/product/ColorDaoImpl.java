package qdh.dao.impl.product;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qdh.dao.entity.product.Color;
import qdh.dao.impl.BaseDAO;

@Repository
public class ColorDaoImpl extends BaseDAO<Color> {
	public List<Color> getColors(List<String> colors) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Color.class);
		Disjunction dis=Restrictions.disjunction();  
		
		for (String color: colors){
            if (!color.trim().equals("")){
				dis.add(Restrictions.like("name", color,MatchMode.ANYWHERE));
			}
		}
		
		criteria.add(dis);
		
		return getByCritera(criteria, true);
	}
	
	public boolean checkColorExist(String color){
		DetachedCriteria criteria = DetachedCriteria.forClass(Color.class);
		criteria.add(Restrictions.eq("name", color));
		
		List<Color> colors = this.getByCritera(criteria, true);
		
		if (colors == null || colors.size() != 1)
			return false;
		else 
			return true;
	}
	
	public Color getColorByName(String colorString) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Color.class);
		criteria.add(Restrictions.eq("name", colorString));
		
		List<Color> colors = this.getByCritera(criteria, true);
		
		return colors.get(0);
	}


}
