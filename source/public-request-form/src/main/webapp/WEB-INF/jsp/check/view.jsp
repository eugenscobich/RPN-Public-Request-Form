<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>

<portlet:actionURL var="find" name="find" windowState="EXCLUSIVE"/>


<div class="public-request-form">
	<h1><spring:message code="Check-iformation-about-application"/></h1>
	<div>
		<c:forEach items="${requestSubjects}" var="requestSubject" varStatus="status">
			<div>
				<input type="radio" value="${requestSubject.index}" name="requestSubject"
				id="requestSubject${requestSubject.id}" class="requestSubject" 
				<c:if test="${status.index == 0}">checked="true"</c:if>>&nbsp;<label for="requestSubject${requestSubject.id}"><c:out 
				value="${requestSubject.name}" /></label>
				<br/>
			</div>
		</c:forEach>
	</div>
	<div>
		<form action="${find}" method="post">
			<span class="format"></span><input type="text" name="id"><button><spring:message code="Check"/></button>
		</form>
	</div>
	<div>
		<p class="output"></p>
	</div>
	<script>	
		$(document).ready(function(){
			var form = $('.public-request-form form');
			var requestSubject = $('.public-request-form input[name=requestSubject]');
			var year = '${year}';
			requestSubject.change(function(){
				var value = $(this).val();
				$('.public-request-form .format').html("&nbsp;" + value + "-" + year + "/&nbsp;");
			});
			$('.public-request-form input[name=requestSubject][checked=true]').change();
			
			form.submit(function(){
				var id = form.find('input[name=id]').val();
				if(id) {
					$.ajax({
						url: form.attr('action'),
						data: {id: id},
						dataType: 'html',
						type: 'POST',
						success: function(data) {
							console.log(data);
							if (data) {
								$('.public-request-form .output').html(data);
							}
						}
					});
				}
				 return false;
			});
		})
	</script>
</div>
