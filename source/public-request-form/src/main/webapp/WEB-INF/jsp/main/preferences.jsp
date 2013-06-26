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

<portlet:actionURL name="save" var="save"/>

<div class="public-request-form">
	<div class="response">
		<div>
			<form action="${save}" method="post">
				<div class="title">
					<label><spring:message code="System-Email"/>:</label>
				</div>
				<div class="content">
					<input type="text" name="systemEmail" value="${systemEmail}">
				</div>
				<br/>
				<div class="title">
					<label><spring:message code="Enable-Form"/>:</label>
				</div>
				<div class="content">
					<input type="checkbox" name="enableForm" <c:if test="${enableForm}">checked="checked"</c:if> value="true">
				</div>
				<br/>
				<div class="title">
					<label><spring:message code="Disabled-Form-Message"/>:</label>
				</div>
				<div class="content">
					<input type="text" name="disbledFormMessage" value="${disbledFormMessage}">
				</div>
				<br/>
				<button><spring:message code="Save"/></button>
			</form>
		</div>
	</div>
</div>
