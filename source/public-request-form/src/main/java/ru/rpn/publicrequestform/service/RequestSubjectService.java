package ru.rpn.publicrequestform.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.rpn.publicrequestform.dao.RequestSubjectDAO;
import ru.rpn.publicrequestform.model.RequestSubject;

@Service
public class RequestSubjectService {
	
	@Autowired
	private RequestSubjectDAO requestSubjectDAO;
	
	@Value("${initial.request.subjects}")
	private String initialRequestSubject;
	
	
	@Transactional(readOnly = true)
	public RequestSubject get(long id) {
		return requestSubjectDAO.find(id);
	}

	@Transactional(readOnly = true)
	public List<RequestSubject> getAll() {
		return requestSubjectDAO.getAll();
	}
	
	@PostConstruct
	private void init() {
		String[] requestSubjects = initialRequestSubject.split(";");
		boolean check = true;
		int i = 1;
		for (String requestSubject : requestSubjects) {
			if (check && requestSubjectDAO.get(requestSubject) != null) {
				break;
			}
			check = false;
			RequestSubject rs = new RequestSubject();
			rs.setIndex(String.format("%02d", i));
			rs.setName(requestSubject);
			requestSubjectDAO.persist(rs);
			i++;
		}
	}

	@Transactional(readOnly = true)
	private RequestSubject get(String requestSubject) {
		return requestSubjectDAO.get(requestSubject);
	}
	
	@Transactional
	public void save(RequestSubject requestSubject) {
		if (requestSubject != null) {
			if (requestSubject.getId() == null) {
				requestSubjectDAO.persist(requestSubject);
			} else {
				requestSubjectDAO.merge(requestSubject);
			}
		}
	}
}
