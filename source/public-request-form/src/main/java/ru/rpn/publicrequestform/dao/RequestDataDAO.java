package ru.rpn.publicrequestform.dao;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ru.rpn.publicrequestform.model.RequestData;

@Repository
public class RequestDataDAO extends BaseDAO<RequestData> {

	@SuppressWarnings("unchecked")
	public List<RequestData> getAll(Long requestSubjectId, Long statusId, Date date) {
		
		String queryString = "select rd from " + RequestData.class.getName() + " rd ";
		
		if (!requestSubjectId.equals(0l)) {
			queryString = queryString + "left join rd.requestSubject rs ";
		}
		
		if (!statusId.equals(0l)) {
			queryString = queryString + "left join rd.status s ";
		}
		
		if (!requestSubjectId.equals(0l) || !statusId.equals(0l) || date != null) {
			queryString = queryString + "where ";
		}
		
		if (!requestSubjectId.equals(0l)) {
			queryString = queryString + "rs.id=" + requestSubjectId + " ";
		}
		
		if (!statusId.equals(0l)) {
			if (!requestSubjectId.equals(0l)) {
				queryString = queryString + "and ";
			}
			queryString = queryString + "s.id=" + statusId + " ";
		}
		
		if (date != null) {
			if (!statusId.equals(0l) || !requestSubjectId.equals(0l)) {
				queryString = queryString + "and ";
			}
			DateFormat dateFormat = DateFormat.getDateInstance();
			queryString = queryString + "rd.date='" + dateFormat.format(date) + "'";
		}
		
		Query query = getEntityManager().createQuery(queryString);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<RequestData>  getAll(String firstName) {
		Query query = getEntityManager().createQuery("from " + RequestData.class.getName() + " rd where rd.firstName LIKE '%" + firstName + "%'");
		return query.getResultList();
		
	}

}
