package ru.rpn.publicrequestform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "prf_status")
public class Status  implements Serializable, Comparable<Status> {
	
	private static final long serialVersionUID = -4935435727026529412L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private Boolean isEnabled;
	
	@Column
	private Boolean isSystem;
	
	@Column
	private Boolean needDate;
	
	@Column
	private Boolean needAddtionalInformation;

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

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
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
		Status other = (Status) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Status o) {
		if (id > o.getId()) {
			return 1;
		} else if (id < o.getId()){
			return -1;
		} else {
			return 0;
		}
	}

	public Boolean getNeedDate() {
		return needDate;
	}

	public void setNeedDate(Boolean needDate) {
		this.needDate = needDate;
	}



	public final Boolean getNeedAddtionalInformation() {
		return needAddtionalInformation;
	}

	public final void setNeedAddtionalInformation(Boolean needAddtionalInformation) {
		this.needAddtionalInformation = needAddtionalInformation;
	}

	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

}
