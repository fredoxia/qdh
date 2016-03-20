package qdh.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import qdh.utility.loggerLocal;
/**
 * the base DAO for resource 2
 * @author fredo
 * @param <T>
 *
 */
public class BaseDAO2<T> extends DAOAbstract implements DAOInterface<T>{

	@Autowired
	private HibernateTemplate hibernateTemplate2;

	public HibernateTemplate getHibernateTemplateMS() {
		return hibernateTemplate2;
	}

	public void setHibernateTemplateMS(HibernateTemplate hibernateTemplate2) {
		this.hibernateTemplate2 = hibernateTemplate2;
	}


	@Override
	public List<T> getByCritera(DetachedCriteria criteria, boolean cached){
		if (cached)
			getHibernateTemplateMS().setCacheQueries(true);
		
		return getHibernateTemplateMS().findByCriteria(criteria);
	}
	
	@Override
	public void saveOrUpdate(T entity, boolean cached) {

	}
	
	@Override
	public T get(Serializable id, boolean cached){
		if (cached)
			getHibernateTemplateMS().setCacheQueries(true);
		return (T)getHibernateTemplateMS().get(entityClass, id);
	}
	@Override
	public void delete(T entity, boolean cached){

	}
	
	/**
	 * to initialized object
	 * @param object
	 */
	@Override
	public void initialize(Object object){
		getHibernateTemplateMS().initialize(object);
	}


	@Override
	public void save(T entity, boolean cached) {

	}

	@Override
	public void update(T entity, boolean cached) {
	
	}

	@Override
	public List<T> getByCritera(DetachedCriteria criteria, int start, int end, boolean cached) {
		if (cached)
			getHibernateTemplateMS().setCacheQueries(true);
		return getHibernateTemplateMS().findByCriteria(criteria, start, end);
	}

	@Override
	public List<T> getAll(boolean cached) {
		if (cached)
			getHibernateTemplateMS().setCacheQueries(true);
		
		return getHibernateTemplateMS().loadAll(entityClass);
	}
	
	@Override
	public List<T> getByHQL(String queryString, Object[] values, boolean cached) {
		getHibernateTemplateMS().setCacheQueries(cached);
		return getHibernateTemplateMS().find(queryString, values);
	}

	@Override
	public int executeHQLUpdateDelete(String queryString, Object[] values,
			boolean cached) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeHQLCount(String queryString, Object[] values,
			boolean cached) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void evict(T entity) {
		getHibernateTemplateMS().evict(entity);
		
	}

	@Override
	public List<Object> getByCriteriaProjection(DetachedCriteria criteria,
			boolean cached) {
		if (cached)
			getHibernateTemplateMS().setCacheQueries(true);

		
		List<Object> resultList = null;
		try{
		     resultList = getHibernateTemplateMS().findByCriteria(criteria);
		} catch (Exception e) {
			loggerLocal.error(e);
		}
		return resultList;
	}
	
	@Override
	public List<Object> executeProcedure(final String procedure, final Object[] values) {
		return getHibernateTemplateMS().execute(new HibernateCallback<List<Object>>() {
			@Override
			public List<Object> doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery  q= session.createSQLQuery(procedure);
				q.setCacheable(false);
            	if (values != null && values.length > 0)
            	  for (int i =0; i < values.length; i++)
    				  q.setParameter(i, values[i]);
            	
    			List<Object> result = q.list();
    			
    			return result;
			}
		});	
	}

	@Override
	public List<Object> executeHQLSelect(String queryString, Object[] values,Integer[] pager,
			boolean cached) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getByCriteriaProjection(DetachedCriteria criteria,
			Integer start, Integer end, boolean cached) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearSession() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(T object) {
		// TODO Auto-generated method stub
		
	}


	
}
