package ru.rpn.publicrequestform.model;

public enum TemplateType {

	SUBMIT("/templates/submit-template.vm"),
	SENDED("/templates/sended-template.vm"), 
	ADMIN_SUBMIT("/templates/admin-submit-template.vm");
	
	private String template;
	
	TemplateType(String template){
		this.setTemplate(template);
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
}
