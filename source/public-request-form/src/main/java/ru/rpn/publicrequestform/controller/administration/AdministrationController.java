package ru.rpn.publicrequestform.controller.administration;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
import ru.rpn.publicrequestform.model.ResponseStatus;
import ru.rpn.publicrequestform.model.Status;
import ru.rpn.publicrequestform.service.DepartmentService;
import ru.rpn.publicrequestform.service.RequestDataService;
import ru.rpn.publicrequestform.service.RequestSubjectService;
import ru.rpn.publicrequestform.service.StatusService;
import ru.rpn.publicrequestform.util.bind.CustomStatusPropertyEditor;

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
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Status.class, customStatusPropertyEditor);
	}
	
	@RenderMapping
	public String view(RenderRequest request, RenderResponse response, Model model) {
		model.addAttribute("requestDatas", requestDataService.getAll());
		return "view";
	}
	
	@RenderMapping(params="view=editRequestData")
	public String editRequestData(RenderRequest request, RenderResponse response, Model model, @RequestParam("id") Long id) {
		List<Status> statuses = statusService.getAllActive();
		List<Department> departments = departmentService.getAllActive();
		RequestData requestData = requestDataService.get(id);
		model.addAttribute("requestData", requestData);
		model.addAttribute("departments", departments);
		model.addAttribute("statuses", statuses);
		return "edit";
	}
	
	@ActionMapping("changeStatus")
	public void changeStatus(ActionRequest request, ActionResponse response, Model model, @RequestParam("id") Long id,
			@RequestParam("status") Long statusId) throws Exception {
		requestDataService.changeStatus(id, statusId);
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
	
	@ActionMapping("changeResponceStatus")
	public void changeResponceStatus(ActionRequest request, ActionResponse response, Model model, @RequestParam("id") Long id,
			@RequestParam("responseStatus") ResponseStatus responseStatus) throws Exception {
		requestDataService.changeResponceStatus(id, responseStatus);
		model.addAttribute("success", "success-change");
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
	
}
