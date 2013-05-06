package ru.rpn.publicrequestform.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseDAO <K extends Serializable> {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void persist(K entity) {
		getEntityManager().persist(entity);
	}

	public K merge(K entity) {
		return getEntityManager().merge(entity);
	}

	public K find(Long id) {
		return getEntityManager().find(getEntityClazz(), id);
	}

	public List<K> getAll() {
		return getEntityManager().createQuery("from " + getEntityClazz().getName(), getEntityClazz()).getResultList();
	}

	public void remove(K entity) {
		getEntityManager().remove(entity);
	}

	@SuppressWarnings("unchecked")
	private Class<K> getEntityClazz() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class<K>) genericSuperclass.getActualTypeArguments()[0];
	}

	protected K getSingleResult(List<K> entities) {
		if (entities != null && !entities.isEmpty()) {
			return entities.get(0);
		}
		return null;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
