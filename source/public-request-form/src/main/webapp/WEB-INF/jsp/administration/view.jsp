<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="public-request-form-administration">
	<c:choose>
		<c:when test="${not empty success}">
			<div class="portlet-msg-success"><spring:message code="success-send-request"/></div>
		</c:when>
		<c:when test="${not empty errors}">
			<div class="portlet-msg-error"><spring:message code="${errors}"/></div>
		</c:when>
	</c:choose>
	
	<table>
		<thead>
			<tr>
				<th><spring:message code="Code"/></th>
				<th><spring:message code="First-Name"/></th>
				<th><spring:message code="Text-Message"/></th>
				<th><spring:message code="Date"/></th>
				<th><spring:message code="Status"/></th>
				<th><spring:message code="Response-Status"/></th>
				<th><spring:message code="Actions"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${requestDatas}" var="requestData" varStatus="status">
				<tr>
					<td>
						<portlet:renderURL var="editRequestData" >
							<portlet:param name="view" value="editRequestData"/>
							<portlet:param name="id" value="${requestData.id}"/>
						</portlet:renderURL>
						<a href="${editRequestData}"><c:out value="${requestData.code}"/></a>
					</td>
					<td><c:out value="${requestData.firstName}"/></td>
					<td><c:out value="${requestData.stripMessage}"/></td>
					<td><fmt:formatDate value="${requestData.date}" pattern="dd.MM.yyyy"/></td>
					<td><c:out value="${requestData.status.name}"/></td>
					<td><spring:message code="ResponseStatus.${requestData.responseStatus}"/></td>
					<td>
						<portlet:actionURL var="deleteRequestData">
							<portlet:param name="id" value="${requestData.id}"/>
						</portlet:actionURL>
						
						
						<span><a href="${editRequestData}" class="icon icon-edit"></a></span>
						<span><a href="${deleteRequestData}" class="icon icon-delete"></a></span>
						
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	

</div>
