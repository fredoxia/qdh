package qdh.dao.impl.product;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.product.NumPerHand;
import qdh.dao.impl.BaseDAO;


@Repository
public class NumPerHandDaoImpl extends BaseDAO<NumPerHand>{

}
