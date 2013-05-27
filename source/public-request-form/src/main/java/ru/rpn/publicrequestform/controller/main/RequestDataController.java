package ru.rpn.publicrequestform.controller.main;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

import ru.rpn.publicrequestform.errors.CaptchaException;
import ru.rpn.publicrequestform.errors.FileSizeException;
import ru.rpn.publicrequestform.model.RequestData;
import ru.rpn.publicrequestform.model.RequestSubject;
import ru.rpn.publicrequestform.service.RequestDataService;
import ru.rpn.publicrequestform.service.RequestSubjectService;
import ru.rpn.publicrequestform.util.bind.CustomRequestSubjectPropertyEditor;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;

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
	public String view(RenderRequest request, RenderResponse response, Model model, @ModelAttribute("requestData") RequestData requestData) {
		return "view";
	}
	
	@RenderMapping(params="view=error")
	public String error(RenderRequest request, RenderResponse response, Model model) {
		return "view";
	}
	
	
	@ActionMapping("send")
	public void send(MultipartActionRequest request, ActionResponse response, Model model, 
			@Valid @ModelAttribute("requestData") RequestData requestData, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			response.setRenderParameter("view", "error");
			return;
		}
		try {
			String systemEmail = PreferencesController.getSystemEmail(request);
			if (StringUtils.isEmpty(systemEmail) || !systemEmail.contains("@")) {
				model.addAttribute("errors", "no-system-email");
				response.setRenderParameter("view", "error");
				return;
			} 
			checkCaptcha(request, response);
			requestDataService.save(requestData, PortalUtil.getCompanyId(request), systemEmail);
			model.addAttribute("requestData", new RequestData());
			model.addAttribute("success", "success");
		} catch (FileSizeException e) {
			LOG.error(e);
			model.addAttribute("errors", "file-size-error");
			response.setRenderParameter("view", "error");
		} catch (CaptchaException e) {
			LOG.error(e);
			model.addAttribute("errors", "captcha-error");
			response.setRenderParameter("view", "error");
		} catch (Exception e) {
			LOG.error(e);
			model.addAttribute("errors", "system-error");
			response.setRenderParameter("view", "error");
		}
	}
	
	@RenderMapping(params="view=success")
	public String success(RenderRequest request, RenderResponse response, Model model) {
		return "success";
	}
	
	@ResourceMapping
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException, PortletException {
		com.liferay.portal.kernel.captcha.CaptchaUtil.serveImage(resourceRequest, resourceResponse);
	}
	
	private void checkCaptcha(PortletRequest request, PortletResponse response) throws Exception {
        String enteredCaptchaText = ParamUtil.getString(request, response.getNamespace() + "captchaText");

        PortletSession session = request.getPortletSession();
        String captchaText = getCaptchaValueFromSession(session);
        if (Validator.isNull(captchaText)) {
            throw new CaptchaException();
        }
        if (!StringUtils.equals(captchaText, enteredCaptchaText)) {
            throw new CaptchaException();
        }
    }

    private String getCaptchaValueFromSession(PortletSession session) {
        Enumeration<String> atNames = session.getAttributeNames();
        while (atNames.hasMoreElements()) {
            String name = atNames.nextElement();
            if (name.contains("CAPTCHA_TEXT")) {
                return (String) session.getAttribute(name);
            }
        }
        return null;
    }
	
}
