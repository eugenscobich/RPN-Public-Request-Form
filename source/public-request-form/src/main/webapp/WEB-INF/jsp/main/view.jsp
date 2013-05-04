<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<portlet:actionURL name="send" var="sendAction"/>

<div class="public-request-form">

	<form:form action="${sendAction}" method="post" modelAttribute="requestData" enctype="multipart/form-data">
		<div class="title">
			<form:label path="firstName"><spring:message code="First-Name"/></form:label>
		</div>
		<div class="content">
			<form:input path="firstName"/><form:errors path="firstName"/>
		</div>
		
		
		<div class="title">
			<form:label path="lastName"><spring:message code="Last-Name"/></form:label>
		</div>
		<div class="content">
			<form:input path="lastName"/><form:errors path="lastName"/>
		</div>
		
		<div class="title">
			<form:label path="middleName"><spring:message code="Middle-Name"/></form:label>
		</div>
		<div class="content">
			<form:input path="middleName"/><form:errors path="middleName"/>
		</div>
		
		<div class="title">
			<form:label path="address"><spring:message code="Address"/></form:label>
		</div>
		<div class="content">
			<form:input path="address"/><form:errors path="address"/>
		</div>
		
		<div class="title">
			<form:label path="email"><spring:message code="Email"/></form:label>
		</div>
		<div class="content">
			<form:input path="email"/><form:errors path="email"/>
		</div>
		
		<div class="title">
			<form:label path="phone"><spring:message code="Phone"/></form:label>
		</div>
		<div class="content">
			<form:input path="phone"/><form:errors path="phone"/>
		</div>
		
		<div class="title">
			<form:label path="requestSubject"><spring:message code="Request-Subject"/></form:label>
		</div>
		<div class="content">
			<form:select path="requestSubject">
				<form:options items="${requestSubjects}" itemLabel="label"/>
			</form:select>
			<form:errors path="requestSubject"/>
		</div>
		
		<div class="title">
			<form:label path="message"><spring:message code="Message"/></form:label>
		</div>
		<div class="content">
			<form:textarea path="message"/><form:errors path="message"/>
		</div>
		
		<div class="title">
			<label><spring:message code="Attachment"/></label>
		</div>
		<div class="content">
			<input name="multipartFiles[0]" type="file"/>
		</div>
		
		<div class="title">
			<label><spring:message code="Attachment"/></label>
		</div>
		<div class="content">
			<input name="multipartFiles[1]" type="file" >
		</div>
		
		<div class="title">
			<label><spring:message code="Attachment"/></label>
		</div>
		<div class="content">
			<input name="multipartFiles[2]" type="file"/>
		</div>
		
		<div class="submit">
			<form:button><spring:message code="Send"/></form:button>
		</div>
	</form:form>
</div>
