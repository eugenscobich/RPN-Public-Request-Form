package ru.rpn.publicrequestform.controller.administration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import ru.rpn.publicrequestform.model.Department;
import ru.rpn.publicrequestform.model.RequestData;
import ru.rpn.publicrequestform.model.RequestSubject;
import ru.rpn.publicrequestform.model.ResponseStatus;
import ru.rpn.publicrequestform.model.Status;
import ru.rpn.publicrequestform.service.DepartmentService;
import ru.rpn.publicrequestform.service.RequestDataService;
import ru.rpn.publicrequestform.service.RequestSubjectService;
import ru.rpn.publicrequestform.service.StatusService;
import ru.rpn.publicrequestform.util.bind.CustomDepartmentPropertyEditor;
import ru.rpn.publicrequestform.util.bind.CustomStatusPropertyEditor;

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

@Controller
@RequestMapping(value = "VIEW")
public class AdministrationController {

	public static final Logger LOG = Logger.getLogger(AdministrationController.class);

	@Autowired
	private RequestDataService requestDataService;
	
	@Autowired
	private RequestSubjectService requestSubjectService;
	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private DepartmentService departmentService;  
	
	@Autowired
	private CustomStatusPropertyEditor customStatusPropertyEditor;
	
	@Autowired
	private CustomDepartmentPropertyEditor customDepartmentPropertyEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Status.class, customStatusPropertyEditor);
		binder.registerCustomEditor(Department.class, customDepartmentPropertyEditor);
	}
	
	@RenderMapping
	public String view(RenderRequest request, RenderResponse response, Model model) {
		Set<RequestData> requestDatas = new TreeSet<RequestData>(requestDataService.getAll());
		Set<Status> statuses = new TreeSet<Status>(statusService.getAllActive());
		for (RequestData requestData : requestDatas) {
			if (requestData.getStatus() != null) {
				statuses.add(requestData.getStatus());
			}	
		}
		List<RequestSubject> requestSubjects = requestSubjectService.getAll();
		model.addAttribute("requestSubjects", requestSubjects);
		model.addAttribute("statuses", statuses);
		model.addAttribute("requestDatas", requestDatas);
		Status receivedStatus = statusService.getReceivedStatus();
		model.addAttribute("receivedStatus", receivedStatus);
		return "view";
	}
	
	@RenderMapping(params="view=editRequestData")
	public String editRequestData(RenderRequest request, RenderResponse response, Model model, @RequestParam("id") Long id) {
		Set<Status> statuses = new TreeSet<Status>(statusService.getAllActive());
		Set<Department> departments = new TreeSet<Department>(departmentService.getAllActive());
		RequestData requestData = requestDataService.get(id);
		if (requestData.getStatus() != null) {
			statuses.add(requestData.getStatus());
		}
		if (requestData.getDepartment() != null) {
			departments.add(requestData.getDepartment());
		}
		model.addAttribute("requestData", requestData);
		model.addAttribute("departments", departments);
		model.addAttribute("statuses", statuses);
		return "edit";
	}
	
	@RenderMapping(params="view=editStatuses")
	public String editStatuses(RenderRequest request, RenderResponse response, Model model) {
		List<Status> systemStatuses = statusService.getAllSystemStatuses();
		List<Status> statuses = statusService.getAllActiveNotSystemStatuses();
		model.addAttribute("systemStatuses", systemStatuses);
		model.addAttribute("statuses", statuses);
		return "edit-statuses";
	}
	
	@RenderMapping(params="view=editDepartments")
	public String editDepartment(RenderRequest request, RenderResponse response, Model model) {
		List<Department> departments = departmentService.getAllActive();
		model.addAttribute("departments", departments);
		return "edit-department";
	}
	
	@ActionMapping("changeStatus")
	public void changeStatus(ActionRequest request, ActionResponse response, Model model, @RequestParam("id") Long id, 
			@RequestParam("status") Long statusId,
			@RequestParam(value = "addtionalInformation", required = false) String addtionalInformation, 
			@RequestParam(value = "d1", required = false) Integer d1,
			@RequestParam(value = "m1", required = false) Integer m1, 
			@RequestParam(value = "y1", required = false) Integer y1) throws Exception {
		Date date = null;
		if (d1 != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, d1);
			calendar.set(Calendar.MONTH, m1);
			calendar.set(Calendar.YEAR, y1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			date = calendar.getTime();
		}
		
		requestDataService.changeStatus(id, statusId, date, addtionalInformation);
		model.addAttribute("success", "success-change");
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editRequestData");
	}
	
	@ActionMapping("changeResponseText")
	public void changeResponseText(ActionRequest request, ActionResponse response, Model model, @RequestParam("id") Long id,
			@RequestParam("responseMessage") String responseMessage) throws Exception {
		requestDataService.changeResponseMessage(id, responseMessage);
		model.addAttribute("success", "success-change");
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editRequestData");
	}
	
	@ActionMapping("changeInternalNumber")
	public void changeInternalNumber(ActionRequest request, ActionResponse response, Model model, @RequestParam("id") Long id,
			@RequestParam("internalNumber") String internalNumber) throws Exception {
		requestDataService.changeInternalNumber(id, internalNumber);
		model.addAttribute("success", "success-change");
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editRequestData");
	}
	
	@ActionMapping("changeResponceStatus")
	public void changeResponceStatus(ActionRequest request, ActionResponse response, Model model, @RequestParam("id") Long id,
			@RequestParam("responseStatus") ResponseStatus responseStatus) throws Exception {
		List<PortletPreferences> portletPreferences = PortletPreferencesLocalServiceUtil.getPortletPreferences();
		String systemEmail = "";
		for (PortletPreferences portletPreferences2 : portletPreferences) {
			if (portletPreferences2.getPortletId().equals("PublicRequestForm_WAR_publicrequestfromportlet")) {
				javax.portlet.PortletPreferences preferences = PortletPreferencesLocalServiceUtil.getPreferences(PortalUtil.getCompanyId(request),
						portletPreferences2.getOwnerId(), portletPreferences2.getOwnerType(), 
						portletPreferences2.getPlid(), portletPreferences2.getPortletId());
				systemEmail = preferences.getValue("systemEmail", PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_USER));
				break;
			}
		}
		if (StringUtils.isEmpty(systemEmail) || !systemEmail.contains("@")) {
			model.addAttribute("errors", "no-system-email");	
		} else {
			requestDataService.changeResponceStatus(id, responseStatus, systemEmail);
			model.addAttribute("success", "success-change");
		}
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editRequestData");
	}
	
	@ActionMapping("changeDepartament")
	public void changeDepartament(ActionRequest request, ActionResponse response, Model model, @RequestParam("id") Long id,
			@RequestParam("department") Long departmentId) throws Exception {
		requestDataService.changeDepartament(id, departmentId);
		model.addAttribute("success", "success-change");
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editRequestData");
	}
	
	
	@ActionMapping("deleteRequestData")
	public void send(ActionRequest request, ActionResponse response, Model model, @RequestParam("id") Long id) {
		try {
			requestDataService.delete(id, PortalUtil.getCompanyId(request));
			model.addAttribute("success", "success-delete");
		} catch (Exception e) {
			model.addAttribute("errors", "errors-delete");
		}
		
	}
	
	@ActionMapping("removeStatus")
	public void removeStatus(ActionRequest request, ActionResponse response, Model model, @RequestParam("status") Long statusId, @RequestParam("id") Long id) {
		statusService.remove(statusId);
		model.addAttribute("success", "success-delete");
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editStatuses");
	}
	
	@ActionMapping("addStatus")
	public void addStatus(ActionRequest request, ActionResponse response, Model model, 
			@RequestParam("status") String statusName,
			@RequestParam(value = "needDate", required = false) Boolean needDate,
			@RequestParam(value = "needAddtionalInformation", required = false) Boolean needAddtionalInformation,
			@RequestParam("id") Long id) {
		statusService.add(statusName, needDate != null ? needDate : Boolean.FALSE, needAddtionalInformation != null ? needAddtionalInformation : Boolean.FALSE);
		model.addAttribute("success", "success-add");
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editStatuses");
	}
	
	@ActionMapping("removeDepartment")
	public void removeDepartment(ActionRequest request, ActionResponse response, Model model, @RequestParam("department") Long departmentId, @RequestParam("id") Long id) {
		departmentService.remove(departmentId);
		model.addAttribute("success", "success-delete");
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editDepartments");
	}
	
	@ActionMapping("addDepartment")
	public void addDepartment(ActionRequest request, ActionResponse response, Model model, @RequestParam("department") String departmentName, @RequestParam("id") Long id) {
		departmentService.add(departmentName);
		model.addAttribute("success", "success-add");
		response.setRenderParameter("id", id.toString());
		response.setRenderParameter("view", "editDepartments");
	}

	@ActionMapping("filter")
	public void filter(ActionRequest request, ActionResponse response, Model model, @RequestParam("requestSubject") Long requestSubjectId,
			@RequestParam("status") Long statusId, @RequestParam("d1") Integer d1, 
			@RequestParam("m1") Integer m1, @RequestParam("y1") Integer y1, @RequestParam(value = "dateEnabled", required = false) Boolean dateEnabled) throws Exception {
		Set<RequestData> requestDatas = new TreeSet<RequestData>(requestDataService.getAll(requestSubjectId, statusId, dateEnabled, d1, m1, y1));
		model.addAttribute("requestDatas", requestDatas);
		response.setRenderParameter("view", "filtered");
	}
	
	
	@ActionMapping("search")
	public void search(ActionRequest request, ActionResponse response, Model model, @RequestParam("firstName") String firstName) throws Exception {
		Set<RequestData> requestDatas = new TreeSet<RequestData>(requestDataService.getAll(firstName));
		model.addAttribute("requestDatas", requestDatas);
		response.setRenderParameter("view", "filtered");
	}
	
	@RenderMapping(params="view=filtered")
	public String filtered(RenderRequest request, RenderResponse response, Model model) {
		@SuppressWarnings("unchecked")
		Set<RequestData> requestDatas = (Set<RequestData>) model.asMap().get("requestDatas");
		Set<Status> statuses = new TreeSet<Status>(statusService.getAllActive());
		for (RequestData requestData : requestDatas) {
			if (requestData.getStatus() != null) {
				statuses.add(requestData.getStatus());
			}	
		}
		List<RequestSubject> requestSubjects = requestSubjectService.getAll();
		model.addAttribute("requestSubjects", requestSubjects);
		model.addAttribute("statuses", statuses);
		model.addAttribute("requestDatas", requestDatas);
		Status receivedStatus = statusService.getReceivedStatus();
		model.addAttribute("receivedStatus", receivedStatus);
		return "view";
	}
	
}
