package ru.rpn.publicrequestform.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	public void save(RequestData requestData, long companyId, String systemEmail) throws Exception {
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
		mailService.sendMail(systemEmail, requestData.getEmail(), subject, content, null);
		velocityContext.put("requestData", requestData);
		velocityContext.put("responseMethod", messageSource.getMessage("ResponseMethod." + requestData.getResponseMethod().name(), null, null));
		content = templateService.getTemplateContent(TemplateType.ADMIN_SUBMIT, velocityContext);
		subject = messageSource.getMessage("template.subject." + TemplateType.ADMIN_SUBMIT.name(), new String[]{requestData.getCode()}, null);
		Map<String, File> attachmentsMap = getAttachementsMap(requestData);
		mailService.sendMail(systemEmail, systemEmail, subject, content, attachmentsMap);
	}
	

	private Map<String, File> getAttachementsMap(RequestData requestData) {
		Map<String, File> attachements = new HashMap<String, File>();
		for (MultipartFile multipartFile : requestData.getMultipartFiles()) {
			File file = null;
			File file2 = null;
			try {
				file = File.createTempFile("tempfile", ".tmp"); // will create files like tempfileXXX.tmp  
				file.deleteOnExit();

				file2 = new File(file.getParentFile(), multipartFile.getOriginalFilename());
			    if(file2.exists()) {
			    	file2.delete();
			    	file2 = new File(file.getParentFile(), multipartFile.getOriginalFilename());
			    }
			    file2.deleteOnExit();
			    boolean success = file.renameTo(file2);
			    if (!success) {
			        continue;
			    }
				FileOutputStream out = new FileOutputStream(file2);
		        IOUtils.copy(multipartFile.getInputStream(), out);
		    } catch (IOException e) {
				e.printStackTrace();
			}
			attachements.put(multipartFile.getOriginalFilename(), file2);
		}
		return attachements;
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
	public void changeStatus(Long id, Long statusId, Date date, String addtionalInformation) {
		RequestData requestData = get(id);
		Status status = statusService.get(statusId);
		requestData.setStatus(status);
		requestData.setChangeStatusDate(date);
		requestData.setAdditionalStatusInformation(addtionalInformation);
		requestDataDAO.merge(requestData);
	}

	@Transactional
	public void changeResponseMessage(Long id, String responseMessage) {
		RequestData requestData = get(id);
		requestData.setResponseMessage(responseMessage);
		requestDataDAO.merge(requestData);
	}

	@Transactional
	public void changeResponceStatus(Long id, ResponseStatus responseStatus, String systemEmail) throws AddressException {
		RequestData requestData = get(id);
		requestData.setResponseStatus(responseStatus);
		requestDataDAO.merge(requestData);
		if (responseStatus == ResponseStatus.SENDED) {
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("response", requestData.getResponseMessage());
			String content = templateService.getTemplateContent(TemplateType.SENDED, velocityContext);
			String subject = messageSource.getMessage("template.subject." + TemplateType.SENDED.name(), new String[]{requestData.getCode()}, null);
			mailService.sendMail(systemEmail, requestData.getEmail(), subject, content, null);
		}
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

	@Transactional
	public void changeInternalNumber(Long id, String internalNumber) {
		RequestData requestData = get(id);
		requestData.setInternalNumber(internalNumber);
		requestDataDAO.merge(requestData);
	}
}
