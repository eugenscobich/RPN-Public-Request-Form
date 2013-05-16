package ru.rpn.publicrequestform.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "prf_request_data")
public class RequestData implements Serializable, Comparable<RequestData> {

	private static final long serialVersionUID = 6414415710291607896L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column
	@NotEmpty
	@Size(max = 250)
	private String firstName;
	
	@Column
	@Size(max = 250)
	private String lastName;
	
	@Column
	@Size(max = 250)
	private String middleName;
	
	@Column
	@NotEmpty
	@Size(max = 250)
	private String address;
	
	@Column
	@Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	@Size(max = 250)
	private String email;
	
	@Column
	@Size(max = 250)
	private String phone;
	
	@ManyToOne
	private RequestSubject requestSubject;

	@ManyToOne
	private Status status;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date changeStatusDate;
	
	@Column
	private String additionalStatusInformation;
	
	@ManyToOne
	private Department department;
	
	@Column
	@NotEmpty
	@Type(type="text")
	private String message;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ResponseStatus responseStatus;
	
	@OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="request_data_id")
	private List<Attachment> attachments;
	
	@Column
	@Type(type="text")
	private String responseMessage;
	
	@Transient
	private List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>() ;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public RequestSubject getRequestSubject() {
		return requestSubject;
	}

	public void setRequestSubject(RequestSubject requestSubject) {
		this.requestSubject = requestSubject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<MultipartFile> getMultipartFiles() {
		return multipartFiles;
	}

	public void setMultipartFiles(List<MultipartFile> multipartFiles) {
		this.multipartFiles = multipartFiles;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Date getChangeStatusDate() {
		return changeStatusDate;
	}

	public void setChangeStatusDate(Date changeStatusDate) {
		this.changeStatusDate = changeStatusDate;
	}

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getCode() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate());
		return getRequestSubject().getIndex() + "-" + Integer.toString(calendar.get(Calendar.YEAR)).substring(2) + "/" + getId();
	}
	
	public String getStripMessage() {
		if (message.length() > 50) {
			return message.substring(0, 50) + "...";
		} else {
			return message;
		}
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public int compareTo(RequestData o) {
		if (date.after(o.getDate())) {
			return -1;
		} else if (date.before(o.getDate())){
			return 1;
		} else {
			if (id > o.getId()) {
				return -1;
			} else {
				return 1;
			}
		}
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
		RequestData other = (RequestData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAdditionalStatusInformation() {
		return additionalStatusInformation;
	}

	public void setAdditionalStatusInformation(String additionalStatusInformation) {
		this.additionalStatusInformation = additionalStatusInformation;
	}

}
