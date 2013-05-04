package ru.rpn.publicrequestform.util.bind;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.rpn.publicrequestform.model.RequestSubject;
import ru.rpn.publicrequestform.service.RequestSubjectService;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomRequestSubjectPropertyEditor extends PropertyEditorSupport {

	@Autowired
	private RequestSubjectService requestSubjectService;
	
	@Override
	public String getAsText() {
		if (getValue() != null && getValue() instanceof RequestSubject) {
			return ((RequestSubject) getValue()).getId().toString();
		}
		return getValue() != null ? getValue().toString() : null;
	}

	@Override
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		if (text != null) {
			RequestSubject manufacturer = requestSubjectService.get(Long.parseLong(text));
			setValue(manufacturer);
		}
	}
}
