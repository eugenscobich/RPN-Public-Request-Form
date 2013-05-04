package ru.rpn.publicrequestform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.rpn.publicrequestform.dao.RequestDataDAO;

@Service
public class RequestDataService {
	
	@Autowired
	private RequestDataDAO requestDataDAO;
}
