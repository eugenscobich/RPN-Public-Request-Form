package ru.rpn.publicrequestform.util.bind;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.rpn.publicrequestform.model.Department;
import ru.rpn.publicrequestform.service.DepartmentService;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomDepartmentPropertyEditor extends PropertyEditorSupport {

	@Autowired
	private DepartmentService departmentService;
	
	@Override
	public String getAsText() {
		if (getValue() != null && getValue() instanceof Department) {
			return ((Department) getValue()).getId().toString();
		}
		return getValue() != null ? getValue().toString() : null;
	}

	@Override
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		if (text != null) {
			Department manufacturer = departmentService.get(Long.parseLong(text));
			setValue(manufacturer);
		}
	}
}
