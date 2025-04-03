<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="customer.passenger.list.passport" path="passenger" choices="${passengers}"/>	
			<acme:submit code="customer.booking.form.button.create" action="/customer/is-from/create?bookingId=${bookingId}"/>
		</jstl:when>	
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}"  >		
			<acme:input-select code="customer.passenger.list.passport" path="passenger" choices="${passengers}" readonly="true"/>	
			<acme:submit code="customer.is-from.form.delete" action="/customer/is-from/delete"/>
		</jstl:when> 	
	</jstl:choose>
	
</acme:form>