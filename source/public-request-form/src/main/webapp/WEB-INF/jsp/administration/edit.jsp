<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>

<%@page import="ru.rpn.publicrequestform.model.RequestData" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="ru.rpn.publicrequestform.model.Attachment" %>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry" %>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil" %>
<%@page import="com.liferay.portal.kernel.util.StringPool" %>
<%@page import="com.liferay.portal.kernel.util.HttpUtil" %>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil" %>


<portlet:actionURL name="save" var="saveAction"/>
<portlet:renderURL var="back"/>

<div class="public-request-form-administration">
	<div>
		<span><a href="${back}">&lt;&lt;<spring:message code="Back"/></a></span>
	</div>
	<c:choose>
		<c:when test="${not empty success}">
			<div class="portlet-msg-success"><spring:message code="success-send-request"/></div>
		</c:when>
		<c:when test="${not empty errors}">
			<div class="portlet-msg-error"><spring:message code="${errors}"/></div>
		</c:when>
	</c:choose>
	
	
	<form:form action="${saveAction}" method="post" modelAttribute="requestData">
		<div>
			<div class="title">
				<form:label path="firstName"><spring:message code="First-Name"/>:</form:label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.firstName}"/></span>
			</div>
		</div>
		<div>
			<div class="title">
				<form:label path="lastName"><spring:message code="Last-Name"/>:</form:label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.lastName}"/></span>
			</div>
		</div>
		<div>
			<div class="title">
				<form:label path="middleName"><spring:message code="Middle-Name"/>:</form:label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.middleName}"/></span>
			</div>
		</div>
		<div>
			<div class="title">
				<form:label path="address"><spring:message code="Address"/>:</form:label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.address}"/></span>
			</div>
		</div>
		<div>
			<div class="title">
				<form:label path="email"><spring:message code="Email"/>:</form:label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.email}"/></span>
			</div>
		</div>
		<div>
			<div class="title">
				<form:label path="phone"><spring:message code="Phone"/>:</form:label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.phone}"/></span>
			</div>
		</div>
		<div>
			<div class="title">
				<form:label path="requestSubject"><spring:message code="Request-Subject"/>:</form:label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.requestSubject.name}"/></span>
			</div>
		</div>
		<div>
			<div class="title">
				<form:label path="message"><spring:message code="Text-Message"/>:</form:label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.message}"/></span>
			</div>
		</div>
		<div>
			<div class="title">
				<label><spring:message code="Files"/>:</label>
			</div>
			<div class="content">
				<%
					RequestData requestData = (RequestData)request.getAttribute("requestData"); 
					ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
					for (Attachment attachment : requestData.getAttachments()) {
						DLFileEntry fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(attachment.getEntryFileId());
					 	fileEntry = fileEntry.toEscapedModel();
					 	long fileEntryId = fileEntry.getFileEntryId();
					 	long folderId = fileEntry.getFolderId();
					 	String title = fileEntry.getTitle();
					 	String fileUrl = themeDisplay.getPortalURL() + themeDisplay.getPathContext() + "/documents/" + fileEntry.getGroupId() + StringPool.SLASH + folderId + StringPool.SLASH + HttpUtil.encodeURL(HtmlUtil.unescape(title));
						%>
							<span><a href="<%=fileUrl%>"><%=HttpUtil.encodeURL(title)%></a>; </span>					
						<%
					}
				%>
			</div>	
		</div>
		
		
		<div class="submit">
			<form:button><spring:message code="Send"/></form:button>
		</div>
	</form:form>
</div>
