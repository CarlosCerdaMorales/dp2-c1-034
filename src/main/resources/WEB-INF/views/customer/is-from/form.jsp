<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="customer.booking.list.locatorCode" path="booking" choices="${bookings}"/>	
	<acme:input-select code="customer.passenger.list.passport" path="passenger" choices="${passengers}"/>	
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/is-from/create"/>
		</jstl:when>	
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode}"  >		
			<acme:submit code="customer.is-from.form.update" action="/customer/is-from/update"/>
			<acme:submit code="customer.is-from.form.publish" action="/customer/is-from/publish"/>
		</jstl:when> 	
	</jstl:choose>
	
</acme:form>
