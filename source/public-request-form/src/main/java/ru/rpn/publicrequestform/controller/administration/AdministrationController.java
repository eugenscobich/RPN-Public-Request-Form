package ru.rpn.publicrequestform.controller.administration;

import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
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
public class AdministrationController {

	public static final Logger LOG = Logger.getLogger(AdministrationController.class);

	@Autowired
	private RequestDataService requestDataService;
	
	@Autowired
	private RequestSubjectService requestSubjectService;
	
	@ModelAttribute("requestDatas")
	private List<RequestData> getRequestDatas() {
		return requestDataService.getAll();
	}
	
	@ModelAttribute("requestData")
	private RequestData getRequestData(@RequestParam(value="id", required=false) Long id) {
		if (id == null) {
			return null;
		}
		RequestData rd = requestDataService.get(id);
		return rd;
	}
	
	@RenderMapping
	public String view(RenderRequest request, RenderResponse response, Model model) {
		return "view";
	}
	
	@RenderMapping(params="view=editRequestData")
	public String editRequestData(RenderRequest request, RenderResponse response, Model model) {
		return "edit";
	}
	
	
	@ActionMapping("send")
	public void send(MultipartActionRequest request, ActionResponse response, Model model, 
			@ModelAttribute("requestData") RequestData requestData, BindingResult bindingResult) throws Exception {
		
		
	}
	
}
