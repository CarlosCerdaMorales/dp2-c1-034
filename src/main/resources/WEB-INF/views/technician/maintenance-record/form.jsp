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
    <acme:input-moment code="technician.maintenance-record.form.label.maintenanceMoment" path="maintenanceMoment"/>
    <acme:input-select code="technician.maintenance-record.form.label.status" path="status" choices="${statuses}"/>
    <acme:input-moment code="technician.maintenance-record.form.label.nextInspectionDue" path="nextInspectionDue"/>
    <acme:input-money code="technician.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
    <acme:input-textbox code="technician.maintenance-record.form.label.notes" path="notes"/>
    <acme:input-select code="technician.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
    
    <jstl:choose>
       <jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete')&& draftMode == true}">
       		<acme:button code="technician.maintenance-record.form.show.involves" action="/technician/task/list?maintenanceRecordId=${maintenanceRecordId}"/>
            <acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>
            <acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish"/>
        	<acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete"/>
        </jstl:when>
        <jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete')&& draftMode == false}">
       		<acme:button code="technician.maintenance-record.form.show.involves" action="/technician/task/list?maintenanceRecordId=${maintenanceRecordId}"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
        </jstl:when>      
    </jstl:choose>
</acme:form>

