<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div>
	<c:choose>
		<c:when test="${not empty requestData}">
			<span><spring:message code="Document-from"/> <b><fmt:formatDate value="${requestData.date}" pattern="dd.MM.yyyy"/></b> <spring:message code="with-number"/>: <b>${requestData.id}</b></span>
			<br/>
			<span><spring:message code="Status"/>: <c:out value="${requestData.status.name}"/> <c:if test="${not empty requestData.changeStatusDate}">
				<fmt:formatDate value="${requestData.changeStatusDate}" pattern="dd.MM.yyyy"/></span>
			</c:if></span>
		</c:when>
		<c:otherwise><spring:message code="No-Result"/></c:otherwise>
	</c:choose>
</div>
