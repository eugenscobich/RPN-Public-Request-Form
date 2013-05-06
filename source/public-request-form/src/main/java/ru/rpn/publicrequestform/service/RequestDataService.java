package ru.rpn.publicrequestform.service;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.service.ServiceContext;

import ru.rpn.publicrequestform.dao.RequestDataDAO;
import ru.rpn.publicrequestform.model.Attachment;
import ru.rpn.publicrequestform.model.RequestData;
import ru.rpn.publicrequestform.model.TemplateType;

@Service
public class RequestDataService {
	
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
	
	@Transactional
	public void save(RequestData requestData, long companyId) throws Exception {
		requestData.setDate(new Date());
		requestDataDAO.persist(requestData);
		List<Attachment> attachments = liferayService.addFiles(requestData, companyId);
		requestData.setAttachments(attachments);
		requestDataDAO.merge(requestData);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(requestData.getDate());
		String requestNumber = requestData.getRequestSubject().getIndex() + "-" + Integer.toString(calendar.get(Calendar.YEAR)).substring(2) + "/" + requestData.getId();
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("requestNumber", requestNumber);
		String content = templateService.getTemplateContent(TemplateType.SUBMIT, velocityContext);
		String subject = messageSource.getMessage("template.subject." + TemplateType.SUBMIT.name(), new String[]{requestNumber}, null);
		mailService.sendMail(requestData.getEmail(), subject, content, null);
		
	}
	
}
