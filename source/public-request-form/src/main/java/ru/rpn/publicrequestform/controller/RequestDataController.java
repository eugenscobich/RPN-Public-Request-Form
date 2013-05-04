package ru.rpn.publicrequestform.controller;

import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

import ru.rpn.publicrequestform.model.RequestData;
import ru.rpn.publicrequestform.model.RequestSubject;
import ru.rpn.publicrequestform.service.RequestDataService;
import ru.rpn.publicrequestform.service.RequestSubjectService;
import ru.rpn.publicrequestform.util.bind.CustomRequestSubjectPropertyEditor;

@Controller
@RequestMapping(value = "VIEW")
public class RequestDataController {

	public static final Logger LOG = Logger.getLogger(RequestDataController.class);

	@Autowired
	private RequestDataService requestDataService;
	
	@Autowired
	private RequestSubjectService requestSubjectService;
	
	@Autowired
	private CustomRequestSubjectPropertyEditor customRequestSubjectPropertyEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(RequestSubject.class, customRequestSubjectPropertyEditor);
	}
	
	
	@ModelAttribute("requestData")
	private RequestData getRequestData() {
		RequestData rd = new RequestData();
		return rd;
	}
	
	@ModelAttribute("requestSubjects")
	private List<RequestSubject> getRequestSubjects() {
		return requestSubjectService.getAll();
	}
	
	
	@RenderMapping
	public String view(RenderRequest request, RenderResponse response, Model model) {
		return "view";
	}
	
	
	@ActionMapping("send")
	public void send(MultipartActionRequest request, ActionResponse response, Model model,
			@Valid @ModelAttribute("requestData") RequestData requestData, BindingResult bindingResult) {
		request.getFile("file1");
		if (bindingResult.hasErrors()) {
			return;
		}
	}
	
	@RenderMapping(params="view=success")
	public String success(RenderRequest request, RenderResponse response, Model model) {
		return "success";
	}
}
