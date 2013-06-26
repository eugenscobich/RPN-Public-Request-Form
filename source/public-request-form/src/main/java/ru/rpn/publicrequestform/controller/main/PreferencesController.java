package ru.rpn.publicrequestform.controller.main;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

@Controller
@RequestMapping(value = "EDIT")
public class PreferencesController {

	public static final Logger LOG = Logger.getLogger(PreferencesController.class);

	@Autowired
	private MessageSource messageSource;
	
	@RenderMapping
	public String view(RenderRequest request, RenderResponse response, Model model) {
		PortletPreferences preferences = request.getPreferences();
		String systemEmail = preferences.getValue("systemEmail", PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_USER));
		String enableForm = preferences.getValue("enableForm", Boolean.TRUE.toString());
		String disbledFormMessage = preferences.getValue("disbledFormMessage", messageSource.getMessage("disabled-form-message", null, null));
		model.addAttribute("systemEmail", systemEmail);
		model.addAttribute("enableForm", Boolean.parseBoolean(enableForm));
		model.addAttribute("disbledFormMessage", disbledFormMessage);
		return "preferences";
	}
	
	
	@ActionMapping("save")
	public void save(ActionRequest request, ActionResponse response, Model model, @RequestParam("systemEmail") String systemEmail,
			@RequestParam(value="enableForm", required=false) String enableForm, @RequestParam("disbledFormMessage") String disbledFormMessage) throws ValidatorException, IOException, ReadOnlyException, WindowStateException, PortletModeException {
		PortletPreferences preferences = request.getPreferences();
		preferences.setValue("systemEmail", systemEmail);
		preferences.setValue("disbledFormMessage", disbledFormMessage);
		if (enableForm == null) {
			preferences.setValue("enableForm", Boolean.FALSE.toString());	
		} else {
			preferences.setValue("enableForm", Boolean.TRUE.toString());
		}
		
		preferences.store();
		response.setPortletMode(PortletMode.VIEW);
		response.setWindowState(WindowState.NORMAL);
	}
	
	public static String getSystemEmail(PortletRequest request) {
		PortletPreferences preferences = request.getPreferences();
		String systemEmail = preferences.getValue("systemEmail", PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_USER));
		return systemEmail;
	}
	
	public static String getEnableForm(PortletRequest request) {
		PortletPreferences preferences = request.getPreferences();
		String systemEmail = preferences.getValue("enableForm", Boolean.TRUE.toString());
		return systemEmail;
	}
	
	public static String getDisbledFormMessage(PortletRequest request, MessageSource messageSource) {
		PortletPreferences preferences = request.getPreferences();
		String disbledFormMessage = preferences.getValue("disbledFormMessage", messageSource.getMessage("disabled-form-message", null, null));
		return disbledFormMessage;
	}
	

}
