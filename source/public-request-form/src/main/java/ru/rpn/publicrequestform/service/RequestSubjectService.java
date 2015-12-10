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
public class RequestSubjectService {
	
	@Autowired
	private RequestSubjectDAO requestSubjectDAO;
	
	@Autowired
	private RequestSubjectDetailService requestSubjectDetailService;
	
	@Autowired
	private RequestSubjectDetailDAO requestSubjectDetailDAO;
	
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
	
	@PostInitialize
	@Transactional
	public void init() {
		String[] requestSubjects = initialRequestSubject.split(";");
		boolean check = true;
		int i = 1;
		for (String requestSubject : requestSubjects) {
			String[] requestSubjectsParts = requestSubject.split("=");	
			String requestSubjectName = requestSubjectsParts[0];
			
			if (check && requestSubjectDAO.get(requestSubjectName) != null) {
				break;
			}
			check = false;
			RequestSubject rs = new RequestSubject();
			rs.setIndex(String.format("%02d", i));
			rs.setName(requestSubjectName);
			save(rs);
			
			
			String[] requestSubjectsDeatils = requestSubjectsParts[1].split("\\|");
			boolean check2 = true;
			for (String requestSubjectsDeatil : requestSubjectsDeatils) {
				if (check2 && requestSubjectDAO.get(requestSubjectsDeatil) != null) {
					break;
				}
				check2 = false;
				RequestSubject rsd = new RequestSubject();
				rsd.setIndex(String.format("%02d", i));
				rsd.setName(requestSubjectsDeatil);
				save(rsd);
			}
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
