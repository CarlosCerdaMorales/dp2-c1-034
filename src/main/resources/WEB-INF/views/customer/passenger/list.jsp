<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.passport" path="passport"/>
	<acme:list-column code="customer.passenger.list.dateOfBirth" path="dateOfBirth"/>
</acme:list>

<jstl:if test="${empty bookingId}">
	<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
</jstl:if>

<jstl:if test="${draftMode == true}">
	<acme:button code="customer.is-from.list.button.create" action="/customer/is-from/create?bookingId=${bookingId}"/>
</jstl:if>