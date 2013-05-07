package ru.rpn.publicrequestform.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.rpn.publicrequestform.dao.RequestDataDAO;
import ru.rpn.publicrequestform.model.Attachment;
import ru.rpn.publicrequestform.model.RequestData;
import ru.rpn.publicrequestform.model.RequestSubject;
import ru.rpn.publicrequestform.model.ResponseStatus;
import ru.rpn.publicrequestform.model.TemplateType;

@Service
public class RequestDataService {
	
	@Value("${default.status}")
	private String defaultStatus;
	
	@Autowired
	private RequestDataDAO requestDataDAO;

	@Autowired
	private LiferayService liferayService;

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
		requestData.setStatus(statusService.get(defaultStatus));
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
	
	
}
