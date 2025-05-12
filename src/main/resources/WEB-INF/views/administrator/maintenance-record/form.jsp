<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.maintenance-record.form.label.date" path="maintenanceMoment"/>
	<acme:input-textbox code="administrator.maintenance-record.form.label.maintenanceStatus" path="status"/>	
	<acme:input-textbox code="administrator.maintenance-record.form.label.nextInspection" path="nextInspectionDue"/>	
	<acme:input-textbox code="administrator.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textbox code="administrator.maintenance-record.form.label.notes" path="notes"/>
	<acme:input-textbox code="administrator.maintenance-record.form.label.aircraft" path="aircraft"/>
 
	<jstl:if test="${_command == 'show'  && draftMode == false}">
			<acme:button code="administrator.maintenance-record.form.button.tasks" action="/administrator/task/list?maintenanceRecordId=${maintenanceRecordId}&draftMode=${draftMode}"/>
	</jstl:if>
</acme:form>