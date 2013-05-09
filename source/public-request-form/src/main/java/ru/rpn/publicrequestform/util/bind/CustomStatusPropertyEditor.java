package ru.rpn.publicrequestform.util.bind;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.rpn.publicrequestform.model.Status;
import ru.rpn.publicrequestform.service.StatusService;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomStatusPropertyEditor extends PropertyEditorSupport {

	@Autowired
	private StatusService statusService;
	
	@Override
	public String getAsText() {
		if (getValue() != null && getValue() instanceof Status) {
			return ((Status) getValue()).getId().toString();
		}
		return getValue() != null ? getValue().toString() : null;
	}

	@Override
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		if (text != null) {
			Status manufacturer = statusService.get(Long.parseLong(text));
			setValue(manufacturer);
		}
	}
}
