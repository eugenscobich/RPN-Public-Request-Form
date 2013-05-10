package ru.rpn.publicrequestform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.rpn.publicrequestform.dao.StatusDAO;
import ru.rpn.publicrequestform.model.Status;
import ru.rpn.publicrequestform.util.spring.PostInitialize;

@Service
public class StatusService {
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Value("${initial.statuses}")
	private String initialSatuses;
	
	@Value("${default.status}")
	private String defaultStatus;
	
	@Transactional(readOnly = true)
	public Status get(long id) {
		return statusDAO.find(id);
	}

	@Transactional(readOnly = true)
	public List<Status> getAll() {
		return statusDAO.getAll();
	}
	
	@PostInitialize
	@Transactional
	public void init() {
		String[] statuses = initialSatuses.split(";");
		boolean check = true;
		for (String status : statuses) {
			if (check && statusDAO.get(status) != null) {
				break;
			}
			check = false;
			Status s = new Status();
			s.setIsEnabled(Boolean.TRUE);
			s.setName(status);
			save(s);
		}
	}

	@Transactional(readOnly = true)
	public Status get(String status) {
		return statusDAO.get(status);
	}
	
	@Transactional
	public void save(Status status) {
		if (status != null) {
			if (status.getId() == null) {
				statusDAO.persist(status);
			} else {
				statusDAO.merge(status);
			}
		}
	}

	public List<Status> getAllActive() {
		return statusDAO.getAllActive();
	}
	
	@Transactional
	public void remove(Long statusId) {
		Status status = get(statusId);
		status.setIsEnabled(Boolean.FALSE);
		statusDAO.merge(status);
	}

	@Transactional
	public void add(String statusName) {
		Status s = new Status();
		s.setIsEnabled(Boolean.TRUE);
		s.setName(statusName);
		save(s);
	}
	
	@Transactional
	public Status getReceivedStatus() {
		return get(defaultStatus);
	}
}
