package ru.rpn.publicrequestform.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ru.rpn.publicrequestform.model.RequestSubject;
import ru.rpn.publicrequestform.model.Subject;

@Repository
public class SubjectDAO extends BaseDAO<Subject> {

	@SuppressWarnings("unchecked")
	public Subject get(String requestSubject) {
		Query query = getEntityManager().createQuery("from " + Subject.class.getName() + " s where s.name = :name");
		query.setParameter("name", requestSubject);
		return getSingleResult(query.getResultList());
	}
}
