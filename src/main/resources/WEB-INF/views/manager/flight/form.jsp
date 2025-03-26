<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="manager.flight.form.label.flightTag" path="flightTag"/>
	<acme:input-checkbox code="manager.flight.form.label.selfTransfer" path="isSelfTransfer"/>
	<acme:input-money code="manager.flight.form.label.flightCost" path="flightCost"/>
	<acme:input-textarea code="manager.flight.form.label.description" path="flightDescription"/>
	<acme:input-moment code="manager.flight.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
	<acme:input-moment code="manager.flight.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
	<acme:input-integer code="manager.flight.form.label.layovers" path="layovers" readonly="true"/>
	<acme:input-textbox code="manager.flight.form.label.departure" path="departure" readonly="true"/>
	<acme:input-textbox code="manager.flight.form.label.arrival" path="arrival" readonly="true"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
				<acme:input-checkbox code="manager.flight.form.label.confirmation" path="confirmation"/>
			<acme:submit code="manager.flight.form.button.update" action="/manager/flight/update"/>
			<acme:submit code="manager.flight.form.button.delete" action="/manager/flight/delete"/>
			<acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="manager.flight.form.label.confirmation" path="confirmation"/>
			<acme:submit code="manager.flight.form.button.create" action="/manager/flight/create"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>
