package ru.rpn.publicrequestform.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ru.rpn.publicrequestform.model.RequestSubject;
import ru.rpn.publicrequestform.model.RequestSubjectDetail;

@Repository
public class RequestSubjectDetailDAO extends BaseDAO<RequestSubjectDetail> {

	@SuppressWarnings("unchecked")
	public RequestSubjectDetail get(String requestSubjectDetail) {
		Query query = getEntityManager().createQuery("from " + RequestSubjectDetail.class.getName() + " rs where rs.name = :name");
		query.setParameter("name", requestSubjectDetail);
		return getSingleResult(query.getResultList());
	}
}
