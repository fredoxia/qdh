package qdh.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class DAOAbstract{
	protected Class entityClass;

	public Class getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	public DAOAbstract(){
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
		entityClass = (Class)params[0];
	}
}
