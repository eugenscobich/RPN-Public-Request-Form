package ru.rpn.publicrequestform.dao;

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
}
