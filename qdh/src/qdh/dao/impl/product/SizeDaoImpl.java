package qdh.dao.impl.product;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.product.Color;
import qdh.dao.entity.product.Size;
import qdh.dao.impl.BaseDAO;

@Repository
public class SizeDaoImpl extends BaseDAO<Size> {



}
