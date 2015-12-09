package ru.rpn.publicrequestform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.rpn.publicrequestform.dao.SubjectDAO;
import ru.rpn.publicrequestform.model.Subject;
import ru.rpn.publicrequestform.util.spring.PostInitialize;

@Service
public class SubjectService {
	
	@Autowired
	private SubjectDAO subjectDAO;
	
	@Value("${initial.subjects}")
	private String initialSubject;
	
	
	@Transactional(readOnly = true)
	public Subject get(long id) {
		return subjectDAO.find(id);
	}

	@Transactional(readOnly = true)
	public List<Subject> getAll() {
		return subjectDAO.getAll();
	}
	
	@PostInitialize
	@Transactional
	public void init() {
		String[] requestSubjects = initialSubject.split(";");
		boolean check = true;
		for (String subject : requestSubjects) {
			if (check && subjectDAO.get(subject) != null) {
				break;
			}
			check = false;
			Subject s = new Subject();
			s.setName(subject);
			save(s);
		}
	}

	@Transactional(readOnly = true)
	private Subject get(String requestSubject) {
		return subjectDAO.get(requestSubject);
	}
	
	@Transactional
	public void save(Subject subject) {
		if (subject != null) {
			if (subject.getId() == null) {
				subjectDAO.persist(subject);
			} else {
				subjectDAO.merge(subject);
			}
		}
	}

}
