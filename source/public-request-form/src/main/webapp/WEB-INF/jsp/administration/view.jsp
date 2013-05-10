<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil" %>

<portlet:actionURL var="filter" name="filter"/>
<portlet:actionURL var="search" name="search"/>


<div class="public-request-form-administration">
	<c:choose>
		<c:when test="${not empty success}">
			<div class="portlet-msg-success"><spring:message code="${success}"/></div>
		</c:when>
		<c:when test="${not empty errors}">
			<div class="portlet-msg-error"><spring:message code="${errors}"/></div>
		</c:when>
	</c:choose>
	<liferay-ui:panel-container>
		<liferay-ui:panel defaultState="closed" title='<%= LanguageUtil.get(pageContext, "filter") %>'>
			<div class="filter">
				<div style="font-weight: bold; padding: 4px;">
					<spring:message code="Filter"/>
				</div>
				<form action="${filter}" method="post" name="fm1">
					<div class="title">
						<label><spring:message code="Request-Subject"/>:</label>
					</div>
					<div class="content">
						<select name="requestSubject">
							<option value="0">--</option>
							<c:forEach items="${requestSubjects}" var="requestSubject">
								<option value="${requestSubject.id}">${requestSubject.label}</option>
							</c:forEach>
						</select>
					</div>
					
					<div class="title">
						<label><spring:message code="Date"/>:</label>
					</div>
					<div class="content">
						<div class="checkbox">
							<input type="checkbox" name="dateEnabled" value="true">
						</div>
						<%
							Calendar calendar = Calendar.getInstance();
							int year = calendar.get(Calendar.YEAR);
							int day = calendar.get(Calendar.DAY_OF_MONTH);
							int maonth = calendar.get(Calendar.MONTH);
						%>
						<liferay-ui:input-date formName="fm1" yearRangeStart="1970"
		            		yearRangeEnd="2100" yearValue="<%=year%>" monthValue="<%=maonth%>" dayValue="<%=day%>"
		            		dayParam="d1" monthParam="m1" yearParam="y1"/>
					</div>
					<div class="title">
						<label><spring:message code="Status"/>:</label>
					</div>
					<div class="content">
						<select name="status">
							<option value="0">--</option>
							<c:forEach items="${statuses}" var="status">
								<option value="${status.id}">${status.name}</option>
							</c:forEach>
						</select>
					</div>
					
					
					<button><spring:message code="Filter"/></button>
				</form>
				<br/>
			</div>
	
			<div class="filter">
				<div style="font-weight: bold; padding: 4px;">
					<spring:message code="Search"/>
				</div>
				<form action="${search}" method="post">
					<div class="title">
						<label><spring:message code="First-Name"/>:</label>
					</div>
					<div class="content">
						<input name="firstName" type="text">
					</div>
					<button><spring:message code="Search"/></button>
				</form>

			</div>
		</liferay-ui:panel>	
	</liferay-ui:panel-container>
	<br/>
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
				<tr <c:if test="${requestData.status == receivedStatus}">class="selected"</c:if>>
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
						<portlet:actionURL var="deleteRequestData" name="deleteRequestData">
							<portlet:param name="id" value="${requestData.id}"/>
						</portlet:actionURL>
						
						
						<span><a href="${editRequestData}" class="icon icon-edit"></a></span>
						<span><a href="${deleteRequestData}" class="icon icon-delete" onclick="confirmDeleteRequest(this); return false;"></a></span>
						
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script>
		function confirmDeleteRequest(a) {
			if(confirm('<spring:message code="Confirm-Delete-Request"/>?')) {
				window.location = a.href;
			} 
		}
	</script>

</div>
