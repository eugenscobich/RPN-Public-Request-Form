package ru.rpn.publicrequestform.service;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import ru.rpn.publicrequestform.model.TemplateType;

@Service
public class TemplateService {

	public String getTemplateContent(TemplateType templateType, VelocityContext velocityContext) {
		Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		Velocity.init();
		Template template = null;
		template = Velocity.getTemplate(templateType.getTemplate(), "UTF-8");
		StringWriter output = new StringWriter();
		if (template != null) {
			template.merge(velocityContext, output);
		}
		return output.toString();
	}
	
}
