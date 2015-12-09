<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>

<portlet:actionURL name="send" var="sendAction"/>
<portlet:resourceURL var="captchaURL" id="captcha"/>

<c:choose>
	<c:when test="${not enableForm}">
		<p><b><c:out value="${disbledFormMessage}"/></b></p>
	</c:when>
	<c:otherwise>
		<div class="public-request-form">
			<c:choose>
				<c:when test="${not empty success}">
					<div class="portlet-msg-success"><spring:message code="success-send-request"/></div>
				</c:when>
				<c:when test="${not empty errors}">
					<div class="portlet-msg-error"><spring:message code="${errors}"/></div>
				</c:when>
			</c:choose>
			
			<form:form action="${sendAction}" method="post" modelAttribute="requestData" enctype="multipart/form-data">
				<div class="title">
					<form:label path="firstName"><spring:message code="First-Name"/> *</form:label>
				</div>
				<div class="content">
					<form:input path="firstName" cssClass="input"/><form:errors path="firstName" cssClass="error"/>
				</div>
				
				
				<div class="title">
					<form:label path="lastName"><spring:message code="Last-Name"/></form:label>
				</div>
				<div class="content">
					<form:input path="lastName" cssClass="input"/><form:errors path="lastName" cssClass="error"/>
				</div>
				
				<div class="title">
					<form:label path="middleName"><spring:message code="Middle-Name"/></form:label>
				</div>
				<div class="content">
					<form:input path="middleName" cssClass="input"/><form:errors path="middleName" cssClass="error"/>
				</div>
				<div class="group">
					<div class="title">
						<form:label path="enterpriseTerritorySubject"><spring:message code="Enterprise-Territory-Subject"/> *</form:label>
					</div>
					<div class="content">
						<form:select path="enterpriseTerritorySubject" cssClass="input">
							<form:options items="${subjects}" itemLabel="name"/>
						</form:select>
						<form:errors path="enterpriseTerritorySubject"  cssClass="error"/>
					</div>
				</div>
				<div class="group">
					<div class="title">
						<form:label path="ownerTerritorySubject"><spring:message code="Owner-Territory-Subject"/> *</form:label>
					</div>
					<div class="content">
						<form:select path="ownerTerritorySubject" cssClass="input">
							<form:options items="${subjects}" itemLabel="name"/>
						</form:select>
						<form:errors path="ownerTerritorySubject"  cssClass="error"/>
					</div>
				</div>
				<div class="title">
					<form:label path="address"><spring:message code="Address"/> *</form:label>
				</div>
				<div class="content">
					<form:input path="address" cssClass="input"/><form:errors path="address" cssClass="error"/>
				</div>
				
				<div class="title">
					<form:label path="email"><spring:message code="Email"/> *</form:label>
				</div>
				<div class="content">
					<form:input path="email" cssClass="input"/><form:errors path="email" cssClass="error"/>
				</div>
				
				<div class="title">
					<form:label path="phone"><spring:message code="Phone"/></form:label>
				</div>
				<div class="content">
					<form:input path="phone" cssClass="input"/><form:errors path="phone" cssClass="error"/>
				</div>
				<div class="title">
					<form:label path="responseMethod"><spring:message code="Social-Status"/></form:label>
				</div>
				<div class="content">
					<spring:eval expression="T(ru.rpn.publicrequestform.model.SocialStatus).values()" var="socialStatuses" />
					<form:select path="socialStatus" cssClass="input">
						<form:option value=""><spring:message code="Select-One"/></form:option>
						<c:forEach items="${socialStatuses}" var="socialStatus">
							<form:option value="${socialStatus}"><spring:message code="SocialStatus.${socialStatus}"/></form:option>
						</c:forEach>
					</form:select>
					<form:errors path="socialStatus" cssClass="error"/>
				</div>
				<div class="title">
					<form:label path="responseMethod"><spring:message code="Response-Method"/></form:label>
				</div>
				<div class="content">
					<spring:eval expression="T(ru.rpn.publicrequestform.model.ResponseMethod).values()" var="responseMethods" />
					<form:select path="responseMethod" cssClass="input">
						<c:forEach items="${responseMethods}" var="responseMethod">
							<form:option value="${responseMethod}"><spring:message code="ResponseMethod.${responseMethod}"/></form:option>
						</c:forEach>
					</form:select>
					<form:errors path="responseMethod" cssClass="error"/>
				</div>
				<div class="title">
					<form:label path="requestSubject"><spring:message code="Request-Subject"/></form:label>
				</div>
				<div class="content">
					<form:select path="requestSubject" cssClass="input">
						<form:options items="${requestSubjects}" itemLabel="label"/>
					</form:select>
					<form:errors path="requestSubject"  cssClass="error"/>
				</div>
				
				<div class="title">
					<form:label path="message"><spring:message code="Text-Message"/> *</form:label>
				</div>
				<div class="content">
					<form:textarea path="message" cssClass="input"/><form:errors path="message" cssClass="error"/>
				</div>
				
				<div class="title">
					<label><spring:message code="Attachment"/></label>
				</div>
				<div class="content">
					<input name="multipartFiles[0]" type="file" class="input"/>
				</div>
				
				<div class="title">
					<label><spring:message code="Attachment"/></label>
				</div>
				<div class="content">
					<input name="multipartFiles[1]" type="file" class="input"/>
				</div>
				
				<div class="title">
					<label><spring:message code="Attachment"/></label>
				</div>
				<div class="content">
					<input name="multipartFiles[2]" type="file" class="input"/>
				</div>
				
				<div><liferay-ui:captcha url="<%= captchaURL %>" /> </div>
				
				<div class="submit">
					<form:button><spring:message code="Send"/></form:button>
				</div>
			</form:form>
		</div>
	</c:otherwise>
</c:choose>
