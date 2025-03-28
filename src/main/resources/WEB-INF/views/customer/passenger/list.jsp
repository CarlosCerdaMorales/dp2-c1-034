<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.passport" path="passport"/>
	<acme:list-column code="customer.passenger.list.dateOfBirth" path="dateOfBirth"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
</jstl:if>