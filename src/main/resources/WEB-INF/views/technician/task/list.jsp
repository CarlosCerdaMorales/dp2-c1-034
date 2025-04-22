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
	<acme:list-column code="technician.task.list.label.type" path="type" width="40%"/>
	<acme:list-column code="technician.task.list.label.priority" path="priority" width="30%"/>
	<acme:list-column code="technician.task.list.label.estimatedDuration" path="estimatedDuration" width="30%"/>
	<acme:list-payload path="payload"/>	
</acme:list>

<jstl:if test="${empty maintenanceRecordId}">
	<acme:button code="technician.task.form.button.create" action="/technician/task/create"/>
</jstl:if>

<jstl:if test="${draftMode == true}">
	<acme:button code="technician.task.form.button.create" action="/technician/involves/create?maintenanceRecordId=${maintenanceRecordId}"/>
	<acme:button code="technician.task.form.button.delete" action="/technician/involves/delete?maintenanceRecordId=${maintenanceRecordId}"/>
</jstl:if>