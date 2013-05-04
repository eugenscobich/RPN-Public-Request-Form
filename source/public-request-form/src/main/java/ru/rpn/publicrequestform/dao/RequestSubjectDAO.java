package ru.rpn.publicrequestform.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ru.rpn.publicrequestform.model.RequestSubject;

@Repository
public class RequestSubjectDAO extends BaseDAO<RequestSubject> {

	@SuppressWarnings("unchecked")
	public RequestSubject get(String requestSubject) {
		Query query = entityManager.createQuery("from " + RequestSubject.class.getName() + " rs where rs.name = :name");
		query.setParameter("name", requestSubject);
		return getSingleResult(query.getResultList());
	}
}
