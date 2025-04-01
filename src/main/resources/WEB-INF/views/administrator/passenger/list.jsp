<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.passenger.list.passport" path="passport"/>
	<acme:list-column code="administrator.passenger.list.dateOfBirth" path="dateOfBirth"/>
</acme:list>