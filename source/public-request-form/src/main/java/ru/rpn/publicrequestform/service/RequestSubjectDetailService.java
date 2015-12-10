package ru.rpn.publicrequestform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.rpn.publicrequestform.dao.RequestSubjectDAO;
import ru.rpn.publicrequestform.dao.RequestSubjectDetailDAO;
import ru.rpn.publicrequestform.model.RequestSubject;
import ru.rpn.publicrequestform.model.RequestSubjectDetail;
import ru.rpn.publicrequestform.util.spring.PostInitialize;

@Service
public class RequestSubjectDetailService {
	
	@Autowired
	private RequestSubjectDetailDAO requestSubjectDetailDAO;
	
	@Transactional(readOnly = true)
	public RequestSubjectDetail get(long id) {
		return requestSubjectDetailDAO.find(id);
	}

	@Transactional(readOnly = true)
	public List<RequestSubjectDetail> getAll() {
		return requestSubjectDetailDAO.getAll();
	}

	@Transactional(readOnly = true)
	private RequestSubjectDetail get(String requestSubjectDetail) {
		return requestSubjectDetailDAO.get(requestSubjectDetail);
	}
	
	@Transactional
	public void save(RequestSubjectDetail requestSubjectDetail) {
		if (requestSubjectDetail != null) {
			if (requestSubjectDetail.getId() == null) {
				requestSubjectDetailDAO.persist(requestSubjectDetail);
			} else {
				requestSubjectDetailDAO.merge(requestSubjectDetail);
			}
		}
	}

}
