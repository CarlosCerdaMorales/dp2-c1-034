<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.fullName" path="fullName"/>
	<acme:list-column code="customer.passenger.list.email" path="email"/>
	<acme:list-column code="customer.passenger.list.passport" path="passport"/>
	<acme:list-column code="customer.passenger.list.dateOfBirth" path="dateOfBirth"/>	
	<acme:list-column code="customer.passenger.list.specialNeeds" path="specialNeeds"/>	
</acme:list>