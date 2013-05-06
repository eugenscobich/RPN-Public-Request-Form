package ru.rpn.publicrequestform.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "prf_request_data")
public class RequestData implements Serializable {

	private static final long serialVersionUID = 6414415710291607896L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column
	@NotEmpty
	private String firstName;
	
	@Column
	private String lastName;
	
	@Column
	private String middleName;
	
	@Column
	@NotEmpty
	private String address;
	
	@Column
	@Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String email;
	
	@Column
	private String phone;
	
	@ManyToOne
	private RequestSubject requestSubject;
	
	@Column
	@NotEmpty
	private String message;
	
	@OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="request_data_id")
	private List<Attachment> attachments;
	
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

}
