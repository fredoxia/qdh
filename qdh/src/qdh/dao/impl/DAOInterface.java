package qdh.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

public interface DAOInterface <T>{
	/**
	 * get the object by criteria
	 * @param criteria
	 * @return
	 */
	public List<T> getByCritera(DetachedCriteria criteria, boolean cached);
	
	/**
	 * get the object by criteria where the start and end points are included
	 * @param criteria
	 * @param start
	 * @param end
	 * @return
	 */
	public List<T> getByCritera(DetachedCriteria criteria, int start, int end, boolean cached);
	
	/**
	 * get the criteria projection
	 * @param criteria
	 * @param cached
	 * @return
	 */
	public List<Object> getByCriteriaProjection(DetachedCriteria criteria, Integer start, Integer end, boolean cached);
	
	/**
	 * get the criteria projection
	 * @param criteria
	 * @param cached
	 * @return
	 */
	public List<Object> getByCriteriaProjection(DetachedCriteria criteria, boolean cached);
	
	
	/**
	 * save or update the object
	 */
	public void saveOrUpdate(T entity, boolean cached);
	
	/**
	 * save the entity
	 * @param entity
	 */
	public void save(T entity, boolean cached);
	
	/**
	 * update the entity
	 * @param entity
	 */
	public void update(T entity, boolean cached);
	
	/**
	 * get the object by id
	 * @param id
	 * @return
	 */
	public T get(Serializable id, boolean cached);

	/**
	 * load all tables
	 * @return
	 */
	public List<T> getAll(boolean cached);
	
	
	/**
	 * delete the record by entity object
	 * @param entity
	 */
	public void delete(T entity, boolean cached);

	
	/**
	 * to initialized object
	 * @param object
	 */
	public void initialize(Object object);

	
	public void evict(T entity);
	
	/**
	 * get the data by hql
	 * @param queryString
	 * @param values
	 * @param cached
	 * @return
	 */
	public List<Object> executeHQLSelect(String queryString, Object[] values,final Integer[] pager,
			boolean cached);
	
	/**
	 * get by hql
	 */
	public List<T> getByHQL(String hql,Object[] values,  boolean cached);

	public List<Object> executeProcedure(String procedure, Object[] values);
	
	public int executeHQLUpdateDelete(String queryString, Object[] values,
			boolean cached);

	/**
	 * get the count
	 * @param queryString
	 * @param values
	 * @param cached
	 * @return
	 */
	public int executeHQLCount(String queryString, Object[] values, boolean cached);
	
	public void clearSession();
}
