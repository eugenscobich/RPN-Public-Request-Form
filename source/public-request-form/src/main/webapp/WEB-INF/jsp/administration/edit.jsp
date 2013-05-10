<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page import="ru.rpn.publicrequestform.model.RequestData" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="ru.rpn.publicrequestform.model.Attachment" %>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry" %>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil" %>
<%@page import="com.liferay.portal.kernel.util.StringPool" %>
<%@page import="com.liferay.portal.kernel.util.HttpUtil" %>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil" %>

<portlet:actionURL name="changeResponseText" var="changeResponseText"/>
<portlet:actionURL name="changeStatus" var="changeStatus"/>
<portlet:actionURL name="changeDepartament" var="changeDepartament"/>
<portlet:actionURL name="changeResponceStatus" var="changeResponceStatus"/>
<portlet:renderURL var="back"/>
<portlet:renderURL var="editDepartment" >
	<portlet:param name="view" value="editDepartments"></portlet:param>
	<portlet:param name="id" value="${requestData.id}"></portlet:param>
</portlet:renderURL>
<portlet:renderURL var="editStatuses" >
	<portlet:param name="view" value="editStatuses"></portlet:param>
	<portlet:param name="id" value="${requestData.id}"></portlet:param>
</portlet:renderURL>

<div class="public-request-form-administration">
	<div>
		<span><a href="${back}">&lt;&lt;<spring:message code="Back"/></a></span>
		<span><a href="${editStatuses}"> <spring:message code="Edit-Statuses"/></a></span>
		<span><a href="${editDepartment}"> <spring:message code="Edit-Departments"/></a></span>
	</div>
	<c:choose>
		<c:when test="${not empty success}">
			<div class="portlet-msg-success"><spring:message code="${success}"/></div>
		</c:when>
		<c:when test="${not empty errors}">
			<div class="portlet-msg-error"><spring:message code="${errors}"/></div>
		</c:when>
	</c:choose>
	
	<div class="group">
		<div class="title">
			<label><spring:message code="Date"/>:</label>
		</div>
		<div class="content">
			<span><fmt:formatDate value="${requestData.date}" pattern="dd.MM.yyyy"/></span>
		</div>
	</div>
	<div class="group">
		<div class="title">
			<label><spring:message code="First-Name"/>:</label>
		</div>
		<div class="content">
			<span><c:out value="${requestData.firstName}"/></span>
		</div>
	</div>
	<div class="group">
		<div class="title">
			<label><spring:message code="Last-Name"/>:</label>
		</div>
		<div class="content">
			<span><c:out value="${requestData.lastName}"/></span>
		</div>
	</div>
	<div class="group">
		<div class="title">
			<label><spring:message code="Middle-Name"/>:</label>
		</div>
		<div class="content">
			<span><c:out value="${requestData.middleName}"/></span>
		</div>
	</div>
	<div class="group">
		<div class="title">
			<label><spring:message code="Address"/>:</label>
		</div>
		<div class="content">
			<span><c:out value="${requestData.address}"/></span>
		</div>
	</div>
	<div class="group">
		<div class="title">
			<label><spring:message code="Email"/>:</label>
		</div>
		<div class="content">
			<span><c:out value="${requestData.email}"/></span>
		</div>
	</div>
	<div class="group">
		<div class="title">
			<label><spring:message code="Phone"/>:</label>
		</div>
		<div class="content">
			<span><c:out value="${requestData.phone}"/></span>
		</div>
	</div>
	<div class="group">
		<div class="title">
			<label><spring:message code="Request-Subject"/>:</label>
		</div>
		<div class="content">
			<span><c:out value="${requestData.requestSubject.label}"/></span>
		</div>
	</div>
	<div class="group">
		<div class="title">
			<label><spring:message code="Text-Message"/>:</label>
		</div>
		<div class="content">
			<span><c:out value="${requestData.message}"/></span>
		</div>
	</div>
	<div class="bottom-dotted">
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
	<div class="response">
		<div>
			<div class="title">
				<label><spring:message code="Response-Message"/>:</label>
			</div>
			<div class="content">
				<form:form action="${changeResponseText}" method="post" modelAttribute="requestData">
					<form:hidden path="id"/>
					<form:textarea path="responseMessage"/>
					<form:button><spring:message code="Save"/></form:button>
				</form:form>
			</div>
		</div>
	
		<div>
			<div class="title" style="line-height: normal;">
				<label><spring:message code="Change-Date"/>:</label>
			</div>
			<div class="content">
				<span><fmt:formatDate value="${requestData.changeStatusDate}" pattern="dd.MM.yyyy"/></span>
			</div>
		</div>
		
		<div>
			<div class="title">
				<label><spring:message code="Change-Status"/>:</label>
			</div>
			<div class="content">
				<form:form action="${changeStatus}" method="post" modelAttribute="requestData">
					<form:hidden path="id"/>
					<form:select path="status" items="${statuses}" itemLabel="name"/>
					<form:button><spring:message code="Save"/></form:button>
				</form:form>
			</div>
		</div>
		
		<div>
			<div class="title">
				<label><spring:message code="Transmit"/>:</label>
			</div>
			<div class="content">
				<form:form action="${changeDepartament}" method="post" modelAttribute="requestData">
					<form:hidden path="id"/>
					<form:select path="department" items="${departments}" itemLabel="name"/>
					<form:button><spring:message code="Save"/></form:button>
				</form:form>
			</div>
		</div>
		
		<div>
			<div class="title">
				<label><spring:message code="Change-Response-Status"/>:</label>
			</div>
			<div class="content">
				<form:form action="${changeResponceStatus}" method="post" modelAttribute="requestData">
					<form:hidden path="id"/>
					<spring:eval expression="T(ru.rpn.publicrequestform.model.ResponseStatus).values()" var="responseStatuses" />
					<form:select path="responseStatus">
						<c:forEach items="${responseStatuses}" var="responseStatus">
							<form:option value="${responseStatus}"><spring:message code="ResponseStatus.${responseStatus}"/></form:option>
						</c:forEach>
					</form:select>
					<form:button><spring:message code="Save"/></form:button>
				</form:form>
			</div>
		</div>
	</div>
</div>
