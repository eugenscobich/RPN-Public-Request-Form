package ru.rpn.publicrequestform.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseDAO <K extends Serializable> {
	
	@PersistenceContext
	public EntityManager entityManager;

	public void persist(K entity) {
		entityManager.persist(entity);
	}

	public K merge(K entity) {
		return entityManager.merge(entity);
	}

	public K find(Long id) {
		return entityManager.find(getEntityClazz(), id);
	}

	public List<K> getAll() {
		return entityManager.createQuery("from " + getEntityClazz().getName(), getEntityClazz()).getResultList();
	}

	public void remove(K entity) {
		entityManager.remove(entity);
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
}
