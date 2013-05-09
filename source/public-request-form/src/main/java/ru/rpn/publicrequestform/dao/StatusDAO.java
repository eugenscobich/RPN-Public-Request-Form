package ru.rpn.publicrequestform.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ru.rpn.publicrequestform.model.Status;

@Repository
public class StatusDAO extends BaseDAO<Status> {

	@SuppressWarnings("unchecked")
	public Status get(String name) {
		Query query = getEntityManager().createQuery("from " + Status.class.getName() + " s where s.name = :name");
		query.setParameter("name", name);
		return getSingleResult(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Status> getAllActive() {
		Query query = getEntityManager().createQuery("from " + Status.class.getName() + " s where s.isEnabled = true");
		return query.getResultList();
	}
}
