package ru.rpn.publicrequestform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "prf_attachment")
public class Attachment implements Serializable {

	private static final long serialVersionUID = 2454548183239314992L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String entryFileId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntryFileId() {
		return entryFileId;
	}

	public void setEntryFileId(String entryFileId) {
		this.entryFileId = entryFileId;
	}
		
	
	
}
