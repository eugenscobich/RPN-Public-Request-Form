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

<portlet:actionURL name="removeDepartment" var="removeDepartment">
	<portlet:param name="id" value="${param.id}"/>
</portlet:actionURL>
<portlet:actionURL name="addDepartment" var="addDepartment">
	<portlet:param name="id" value="${param.id}"/>
</portlet:actionURL>
<portlet:renderURL var="back">
	<portlet:param name="view" value="editRequestData"/>
	<portlet:param name="id" value="${param.id}"/>
</portlet:renderURL>

<div class="public-request-form-administration">
	<div>
		<span><a href="${back}">&lt;&lt;<spring:message code="Back"/></a></span>
	</div>
	<c:choose>
		<c:when test="${not empty success}">
			<div class="portlet-msg-success"><spring:message code="${success}"/></div>
		</c:when>
		<c:when test="${not empty errors}">
			<div class="portlet-msg-error"><spring:message code="${errors}"/></div>
		</c:when>
	</c:choose>
	<div class="response">
		<div>
			<div class="title">
				<label><spring:message code="Edit-Departments"/>:</label>
			</div>
			<div class="content">
				<form action="${removeDepartment}" method="post" onsubmit="return confirm('<spring:message code="Confirm-Delete-Department"/>?')" id="removeDepartment">
					<select name="department">
						<c:forEach items="${departments}" var="department">
							<option value="${department.id}">${department.name}</option>
						</c:forEach>
					</select>
					<button><spring:message code="Delete"/></button>
				</form>
			</div>
			
			<div class="title">
				<label><spring:message code="Add-Department"/>:</label>
			</div>
			<div class="content">
				<form action="${addDepartment}" method="post">
					<input type="text" name="department">
					<button><spring:message code="Save"/></button>
				</form>
			</div>
		</div>
	</div>
</div>
