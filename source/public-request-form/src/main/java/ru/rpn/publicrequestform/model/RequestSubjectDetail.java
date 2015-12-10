package ru.rpn.publicrequestform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//@Entity
//@Table(name = "prf_request_subject_detail")
public class RequestSubjectDetail implements Serializable {

	private static final long serialVersionUID = 5067722166807054249L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@OneToMany
	private RequestSubject requestSubject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return requestSubject.getIndex() + " - " + name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestSubjectDetail other = (RequestSubjectDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public RequestSubject getRequestSubject() {
		return requestSubject;
	}

	public void setRequestSubject(RequestSubject requestSubject) {
		this.requestSubject = requestSubject;
	}


	
}
