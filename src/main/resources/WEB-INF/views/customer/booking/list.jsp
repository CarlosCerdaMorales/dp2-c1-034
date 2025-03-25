<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.booking.list.locatorCode" path="locatorCode" width="25%"/>
	<acme:list-column code="customer.booking.list.purchaseMoment" path="purchaseMoment" width="25%"/>
	<acme:list-column code="customer.booking.list.travelClass" path="travelClass" width="25%"/>
	<acme:list-column code="customer.booking.list.price" path="price" width="25%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="employer.job.list.button.create" action="/employer/job/create"/>
</jstl:if>	