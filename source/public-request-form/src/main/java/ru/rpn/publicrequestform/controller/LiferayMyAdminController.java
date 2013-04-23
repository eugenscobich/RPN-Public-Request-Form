package ru.rpn.publicrequestform.controller;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping(value = "VIEW")
public class LiferayMyAdminController {

	public static final Logger LOG = Logger.getLogger(LiferayMyAdminController.class);

	@RenderMapping
	public String view(RenderRequest request, RenderResponse response, Model model) {
		return "view";
	}
}
