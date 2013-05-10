package ru.rpn.publicrequestform.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.rpn.publicrequestform.dao.RequestDataDAO;
import ru.rpn.publicrequestform.model.Attachment;
import ru.rpn.publicrequestform.model.Department;
import ru.rpn.publicrequestform.model.RequestData;
import ru.rpn.publicrequestform.model.ResponseStatus;
import ru.rpn.publicrequestform.model.Status;
import ru.rpn.publicrequestform.model.TemplateType;

@Service
public class RequestDataService {
	

	
	@Autowired
	private RequestDataDAO requestDataDAO;

	@Autowired
	private LiferayService liferayService;

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private StatusService statusService;
	
	@Transactional
	public void save(RequestData requestData, long companyId) throws Exception {
		Date now = new Date();
		requestData.setDate(now);
		requestData.setStatus(statusService.getReceivedStatus());
		requestData.setResponseStatus(ResponseStatus.NO);
		requestData.setChangeStatusDate(now);
		requestDataDAO.persist(requestData);
		List<Attachment> attachments = liferayService.addFiles(requestData, companyId);
		requestData.setAttachments(attachments);
		requestDataDAO.merge(requestData);
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("requestNumber", requestData.getCode());
		String content = templateService.getTemplateContent(TemplateType.SUBMIT, velocityContext);
		String subject = messageSource.getMessage("template.subject." + TemplateType.SUBMIT.name(), new String[]{requestData.getCode()}, null);
		mailService.sendMail(requestData.getEmail(), subject, content, null);
	}
	

	@Transactional(readOnly = true)
	public List<RequestData> getAll() {
		return requestDataDAO.getAll();
	}

	@Transactional(readOnly = true)
	public RequestData get(Long id) {
		return requestDataDAO.find(id);
	}

	@Transactional
	public void delete(Long id, long companyId) throws Exception {
		RequestData requestData = get(id);
		liferayService.deleteFiles(requestData, companyId);
		requestDataDAO.remove(requestData);
	}

	@Transactional
	public void changeStatus(Long id, Long statusId) {
		RequestData requestData = get(id);
		Status status = statusService.get(statusId);
		requestData.setStatus(status);
		requestData.setChangeStatusDate(new Date());
		requestDataDAO.merge(requestData);
	}

	@Transactional
	public void changeResponseMessage(Long id, String responseMessage) {
		RequestData requestData = get(id);
		requestData.setResponseMessage(responseMessage);
		requestDataDAO.merge(requestData);
	}

	@Transactional
	public void changeResponceStatus(Long id, ResponseStatus responseStatus) {
		RequestData requestData = get(id);
		requestData.setResponseStatus(responseStatus);
		requestDataDAO.merge(requestData);
	}

	@Transactional
	public void changeDepartament(Long id, Long departmentId) {
		RequestData requestData = get(id);
		Department department = departmentService.get(departmentId);
		requestData.setDepartment(department);
		requestDataDAO.merge(requestData);
	}

	@Transactional
	public List<RequestData> getAll(Long requestSubjectId, Long statusId, Boolean dateEnabled, Integer day, Integer month, Integer year) {
		Date date = null;
		if (dateEnabled != null && dateEnabled) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			date = calendar.getTime();
		}
		return requestDataDAO.getAll(requestSubjectId, statusId, date);
	}


	public List<RequestData> getAll(String firstName) {
		return requestDataDAO.getAll(firstName);
	}
}
