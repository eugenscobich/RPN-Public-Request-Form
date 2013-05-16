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
		String[] statuses = initialSatuses.split("\\|");
		boolean check = true;
		for (String statusConfiguration : statuses) {
			String[] status = statusConfiguration.split(";");
			if (check && statusDAO.get(status[0]) != null) {
				break;
			}
			check = false;
			Status s = new Status();
			s.setIsEnabled(Boolean.TRUE);
			s.setName(status[0]);
			s.setIsSystem(Boolean.TRUE);
			if (status[1].equals("1")) {
				s.setNeedDate(Boolean.TRUE);	
			} else {
				s.setNeedDate(Boolean.FALSE);
			}
			if (status[2].equals("1")) {
				s.setNeedAddtionalInformation(Boolean.TRUE);	
			} else {
				s.setNeedAddtionalInformation(Boolean.FALSE);
			}
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
	public void add(String statusName, Boolean needDate, Boolean needAddtionalInformation) {
		Status s = new Status();
		s.setIsSystem(Boolean.FALSE);
		s.setIsEnabled(Boolean.TRUE);
		s.setName(statusName);
		s.setNeedAddtionalInformation(needAddtionalInformation);
		s.setNeedDate(needDate);
		save(s);
	}
	
	@Transactional
	public Status getReceivedStatus() {
		return get(defaultStatus);
	}

	public List<Status> getAllSystemStatuses() {
		return statusDAO.getAllSystemStatuses();
	}

	public List<Status> getAllActiveNotSystemStatuses() {
		return statusDAO.getAllActiveNotSystemStatuses();
	}
}
