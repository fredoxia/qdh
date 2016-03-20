package qdh.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import qdh.utility.loggerLocal;



/**
 * the base DAO for MySQL server
 * @author fredo
 *
 * @param <T>
 */
public class BaseDAO<T> extends DAOAbstract implements DAOInterface<T>{

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public List<T> getByCritera(DetachedCriteria criteria, boolean cached){
		if (cached)
			getHibernateTemplate().setCacheQueries(true);
		else
			getHibernateTemplate().setCacheQueries(false);
		
		List<T> resultList = null;
		try{
		     resultList = getHibernateTemplate().findByCriteria(criteria);
		} catch (Exception e) {
			loggerLocal.error(e);
		}
		return resultList;
	}
	
	@Override
	public List<Object> getByCriteriaProjection(DetachedCriteria criteria, boolean cached){
		if (cached)
			getHibernateTemplate().setCacheQueries(true);
		else
			getHibernateTemplate().setCacheQueries(false);
		
		List<Object> resultList = null;
		try{
			resultList = getHibernateTemplate().findByCriteria(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			loggerLocal.error(e);
		}
		return resultList;
	}
	
	@Override
	public List<Object> getByCriteriaProjection(DetachedCriteria criteria, Integer start, Integer end, boolean cached){
		if (cached)
			getHibernateTemplate().setCacheQueries(true);
		else
			getHibernateTemplate().setCacheQueries(false);
		
		List<Object> resultList = null;
		try{
			if (start != null && end != null)
		        resultList = getHibernateTemplate().findByCriteria(criteria).subList(start.intValue(), end.intValue());
			else 
				resultList = getHibernateTemplate().findByCriteria(criteria);
		} catch (Exception e) {
			loggerLocal.error(e);
		}
		return resultList;
	}
	
	@Override
	public void evict(T entity){
		getHibernateTemplate().evict(entity);
	}
	
	@Override
	public void saveOrUpdate(T entity, boolean cached) {
		if (cached)
			getHibernateTemplate().setCacheQueries(true);

		getHibernateTemplate().saveOrUpdate(entity);

	}
	
	@Override
	public T get(Serializable id, boolean cached){
		if (cached)
			getHibernateTemplate().setCacheQueries(true);
		
		T result = null;
		try{
			result =(T)getHibernateTemplate().get(entityClass, id);
		} catch (Exception e) {
			loggerLocal.error(e);
		}
		
		return result;
	}
	@Override
	public void delete(T entity, boolean cached){
		if (cached)
			getHibernateTemplate().setCacheQueries(true);
		getHibernateTemplate().delete(entity);

	}
	
	/**
	 * to initialized object
	 * @param object
	 */
	@Override
	public void initialize(Object object){
		getHibernateTemplate().initialize(object);
	}


	@Override
	public void save(T entity, boolean cached) {
		if (cached)
			getHibernateTemplate().setCacheQueries(true);
		
		getHibernateTemplate().save(entity);

	}

	@Override
	public void update(T entity, boolean cached) {
		if (cached)
			getHibernateTemplate().setCacheQueries(true);
		getHibernateTemplate().update(entity);

	}

	@Override
	public List<T> getByCritera(DetachedCriteria criteria, int start, int maxRecords, boolean cached) {
		getHibernateTemplate().setCacheQueries(cached);
		
		List<T> resultList = null;
		try{
		     resultList = getHibernateTemplate().findByCriteria(criteria, start, maxRecords);
		} catch (Exception e) {
			e.printStackTrace();
//			loggerLocal.error(e);
		}
		
		return resultList;
	}

	@Override
	public List<T> getAll(boolean cached) {
		getHibernateTemplate().setCacheQueries(cached);

		List<T> resultList = null;
		try{
		     resultList = getHibernateTemplate().loadAll(entityClass);
		} catch (Exception e) {
			loggerLocal.error(e);
		}
		
		return resultList;
	}
	
	@Override
	public List<T> getByHQL(String queryString, Object[] values, boolean cached) {
		getHibernateTemplate().setCacheQueries(cached);
		
		List<T> resultList = null;
		try{
		     resultList = getHibernateTemplate().find(queryString, values);
		} catch (Exception e) {
			loggerLocal.error(e);
		}
		
		return resultList;
	}
	
	@Override	
	public int executeHQLUpdateDelete(final String queryString, final Object[] values, boolean cached){
		getHibernateTemplate().setCacheQueries(cached);
		
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
            	Query q= session.createQuery(queryString);
            	if (values != null && values.length > 0)
            	  for (int i =0; i < values.length; i++)
    				  q.setParameter(i, values[i]);
            	
    			int success = q.executeUpdate();
				return success;
			}
		});	
	}
	
	@Override	
	public int executeHQLCount(final String queryString, final Object[] values, boolean cached){
		getHibernateTemplate().setCacheQueries(cached);
		
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
            	Query q= session.createQuery(queryString);
            	if (values != null && values.length > 0)
            	  for (int i =0; i < values.length; i++)
    				  q.setParameter(i, values[i]);
            	
    			List<Object> result = q.list();
    			int count = ((Long)result.get(0)).intValue();

				return count;
			}
		});	
	}

	
	@Override
	public List<Object> executeHQLSelect(final String queryString, final Object[] values, final Integer[] pager,
			boolean cached) {
		getHibernateTemplate().setCacheQueries(cached);
		
		return getHibernateTemplate().execute(new HibernateCallback<List<Object>>() {
			@Override
			public List<Object> doInHibernate(Session session) throws HibernateException,
					SQLException {
            	Query q= session.createQuery(queryString);
            	if (values != null && values.length > 0)
            	  for (int i =0; i < values.length; i++)
    				  q.setParameter(i, values[i]);
            	
            	if (pager != null){
            		q.setFirstResult(pager[0]);
            		q.setMaxResults(pager[1]);
            	}
            	
    			List<Object> result = q.list();
    			
    			return result;
			}
		});	
	}
	
	@Override
	public List<Object> executeProcedure(final String procedure, final Object[] values) {
		return getHibernateTemplate().execute(new HibernateCallback<List<Object>>() {
			@Override
			public List<Object> doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery  q= session.createSQLQuery(procedure);
		    	   
            	if (values != null && values.length > 0)
            	  for (int i =0; i < values.length; i++)
    				  q.setParameter(i, values[i]);
            	
    			List<Object> result = q.list();
    			
    			return result;
			}
		});	
	}

	public void saveUpdateLargeData(final T object) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				session.saveOrUpdate(object);
				return 1;
			}
		});	
	}
	
	@Override
	public void clearSession(){
		getHibernateTemplate().setCacheQueries(false);
		
		getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session){
            	session.clear();
				return 1;
			}
		});	
	}
	
	@Override
	public void merge(T object){
		getHibernateTemplate().merge(object);
	}
	
}
