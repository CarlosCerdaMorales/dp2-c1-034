<%--
- list.jsp
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

<acme:list>
	<acme:list-column code="technician.involves.list.label.maintenanceRecord-aircraft" path="maintenanceRecord.aircraft.registrationNumber" width="30%"/>	
	<acme:list-column code="technician.involves.list.label.maintenanceRecord.maintenanceMoment" path="maintenanceRecord.maintenanceMoment" width="50%"/>
	<acme:list-column code="technician.involves.list.label.task.type" path="task.type" width="50%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.involves.list.button.create" action="/technician/involves/create"/>
</jstl:if>