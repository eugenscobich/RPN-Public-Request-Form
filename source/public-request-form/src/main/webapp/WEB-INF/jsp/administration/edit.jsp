<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page import="java.util.Calendar"%>
<%@page import="ru.rpn.publicrequestform.model.RequestData" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="ru.rpn.publicrequestform.model.Attachment" %>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry" %>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil" %>
<%@page import="com.liferay.portal.kernel.util.StringPool" %>
<%@page import="com.liferay.portal.kernel.util.HttpUtil" %>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil" %>

<portlet:actionURL name="changeResponseText" var="changeResponseText"/>
<portlet:actionURL name="changeStatus" var="changeStatus"/>
<portlet:actionURL name="changeDepartament" var="changeDepartament"/>
<portlet:actionURL name="changeResponceStatus" var="changeResponceStatus"/>
<portlet:actionURL name="changeInternalNumber" var="changeInternalNumber"/>
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
		<span><a href="#" id="printBtn"> <spring:message code="Print"/></a></span>
	</div>
	<c:choose>
		<c:when test="${not empty success}">
			<div class="portlet-msg-success"><spring:message code="${success}"/></div>
		</c:when>
		<c:when test="${not empty errors}">
			<div class="portlet-msg-error"><spring:message code="${errors}"/></div>
		</c:when>
	</c:choose>
	<div id="printArea">
		<div class="group">
			<div class="title">
				<label><spring:message code="Request-Number"/>:</label>
			</div>
			<div class="content">
				<span style="float: left;"><c:out value="${requestData.code}"/>&nbsp;&nbsp;</span>
				<span id="internal-number-toggle"><a href="#" class="icon icon-open"></a></span>
			</div>
		</div>
		
		<div id="internal-number" style="display: none;" class="response">
			<div class="group" style="margin: 0">
				<div class="title">
					<label><spring:message code="Internal-Number"/>:</label>
				</div>
				<div class="content">
					<form:form action="${changeInternalNumber}" method="post" modelAttribute="requestData">
						<form:hidden path="id"/>
						<form:input path="internalNumber"/>
						<form:button><spring:message code="Save"/></form:button>
					</form:form>
				</div>
			</div>
		</div>
	

		
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
				<label><spring:message code="Enterprise-Territory-Subject"/>:</label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.enterpriseTerritorySubject.name}"/></span>
			</div>
		</div>
		
		<div class="group">
			<div class="title">
				<label><spring:message code="Owner-Territory-Subject"/>:</label>
			</div>
			<div class="content">
				<span><c:out value="${requestData.ownerTerritorySubject.name}"/></span>
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
				<label><spring:message code="Social-Status"/>:</label>
			</div>
			<div class="content">
				<span><c:if test="${not empty requestData.socialStatus}"><spring:message code="SocialStatus.${requestData.socialStatus}"/></c:if></span>
			</div>
		</div>
		
		<div class="group">
			<div class="title">
				<label><spring:message code="Response-Method"/>:</label>
			</div>
			<div class="content">
				<span><spring:message code="ResponseMethod.${requestData.responseMethod}"/></span>
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
				<p style="margin: 0"><c:out value="${requestData.formatedMessage}" escapeXml="false"/></p>
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
	</div>
	<div class="response">
		<div class="group">
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
	
		<div class="group">
			<form:form action="${changeStatus}" method="post" modelAttribute="requestData" name="fm1">
				<div class="title">
					<label><spring:message code="Change-Status"/>:</label>
				</div>
				<div class="content">
					<form:hidden path="id"/>
					<form:select path="status" id="changeStatusSelectBox">
						<c:forEach items="${statuses}" var="status">
							<option value="${status.id}"
								<c:if test="${status eq requestData.status}">selected="true"</c:if> 
								data-needDate="${status.needDate}" 
								data-needAddtionalInformation="${status.needAddtionalInformation}"><c:out value="${status.name}"/></option>
						</c:forEach>
					</form:select>
					<form:button id="changeStatusSubmitButton"><spring:message code="Save"/></form:button>
				</div>
				<div id="changeStatusWraper">
					
				</div>
				
			</form:form>
			<div id="changeStatusDate" style="display: none;">
				<div class="title">
					<label><spring:message code="Status-Date"/>:</label>
				</div>
				<div class="content">
					<%
						Calendar calendar = Calendar.getInstance();
						if (requestData.getChangeStatusDate() != null) {
							calendar.setTime(requestData.getChangeStatusDate());
						}
						int year = calendar.get(Calendar.YEAR);
						int day = calendar.get(Calendar.DAY_OF_MONTH);
						int maonth = calendar.get(Calendar.MONTH);
					%>
					<liferay-ui:input-date formName="fm1" yearRangeStart="1970"
		           		yearRangeEnd="2100" yearValue="<%=year%>" monthValue="<%=maonth%>" dayValue="<%=day%>"
		           		dayParam="d1" monthParam="m1" yearParam="y1"/>
	           	</div>
			</div>
			<div id="changeStatusAdditionalInformation" style="display: none;">
				<div class="title">
					<label><spring:message code="Status-Additional-Information"/>:</label>
				</div>
				<div class="content">
					<input type="text" name="addtionalInformation" value="${requestData.additionalStatusInformation}">
				</div>
			</div>
		</div>
		
		<div class="group">
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
		
		<div class="group">
			<div class="title">
				<label><spring:message code="Change-Response-Status"/>:</label>
			</div>
			<div class="content">
				<form:form action="${changeResponceStatus}" method="post" modelAttribute="requestData" id="changeResponceStatusForm">
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
	<script>
	
		$(function(){
			var changeStatusSelectBox = $('#changeStatusSelectBox');
			var changeStatusWraper = $('#changeStatusWraper');
			var changeStatusDate = $('#changeStatusDate');
			var changeStatusAdditionalInformation = $('#changeStatusAdditionalInformation');
			var changeStatusSubmitButton = $('#changeStatusSubmitButton');
			changeStatusSelectBox.change(function() {
				changeStatusWraper.empty();
				changeStatusSubmitButton.show();
				var value = changeStatusSelectBox.find(":selected");
				var needDate = value.attr('data-needDate');
				var needAddtionalInformation = value.attr('data-needAddtionalInformation');
				if (needDate == "true") {
					changeStatusWraper.prepend(changeStatusDate.clone());
					changeStatusWraper.find('#changeStatusDate').show();
				}
				if (needAddtionalInformation == "true") {
					changeStatusWraper.append(changeStatusAdditionalInformation.clone());
					changeStatusWraper.find('#changeStatusAdditionalInformation').show();
				}
				if (needAddtionalInformation == "true") {
					changeStatusSubmitButton.hide();
					changeStatusWraper.find('#changeStatusAdditionalInformation .content').append(changeStatusSubmitButton.clone());
					changeStatusWraper.find('#changeStatusSubmitButton').show();
				} else if (needDate == "true") {
					changeStatusSubmitButton.hide();
					changeStatusWraper.find('#changeStatusDate .content').append(changeStatusSubmitButton.clone());
					changeStatusWraper.find('#changeStatusSubmitButton').show();
				}
			});
			changeStatusSelectBox.change();
			
			var changeResponceStatusForm = $('#changeResponceStatusForm');
			changeResponceStatusForm.submit(function(){
				var select = $(this).find('select[name=responseStatus]');
				if (select.val() == 'SENDED') {
					if(confirm('<spring:message code="Confirm-Submit-Response-Status"/>?')) {
						return true;
					} else {
						return false;
					} 
				} 
				return true;
			});
			
			
			var $printBtn = $('#printBtn');
			$printBtn.click(function(){
				/*
				$("#printArea").printThis({
				      debug: false,              
				      importCSS: true,           
				      printContainer: true,      
				      loadCSS: '<c:url value="/css/style.css"/>', 
				      pageTitle: "Print",             
				      removeInline: false
					});
				*/
				
				var $data = $('#printArea').clone();
				$data.addClass('public-request-form-administration');
				var mywindow = window.open('', 'Print', 'height=600,width=800');
		        mywindow.document.write('<html><head><title>Print</title>');
		        mywindow.document.write('<link rel="stylesheet" href="<c:url value="/css/style.css"/>" type="text/css" />');
		        //mywindow.document.write('<script src="<c:url value="/js/jquery-1.9.1.min.js"/>" />');
		        mywindow.document.write('</head><body>');
		        mywindow.document.write($data.wrap('<div>').parent().html());
		        
		        mywindow.document.write('<script>setTimeout("window.print();", 1000);');
		        mywindow.document.write('</scr' + 'ipt>');
		        mywindow.document.write('</body></html>');
		        

		        //mywindow.print();
		        //mywindow.close();

		        
		        
		        return true;
			});
			
			
			var $internalNumberToggle=$('#internal-number-toggle');
			var $internalNumber=$('#internal-number');
			
			$internalNumberToggle.click(function(){
				if ($internalNumber.is(':visible')) {
					$internalNumber.hide();
					$internalNumberToggle.find('a').removeClass('icon-close');
					$internalNumberToggle.find('a').addClass('icon-open');
				} else {
					$internalNumber.show();
					$internalNumberToggle.find('a').removeClass('icon-open');
					$internalNumberToggle.find('a').addClass('icon-close');				}
				
				 //$internalNumber.toggle();
			});
			
			
		});
	</script>
</div>
