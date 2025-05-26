<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.maintenance-record.list.label.date" path="maintenanceMoment" width="25%"/>
	<acme:list-column code="administrator.maintenance-record.list.label.maintenanceStatus" path="status" width="25%"/>
	<acme:list-column code="administrator.maintenance-record.list.label.nextInspection" path="nextInspectionDue" width="25%"/>
	<acme:list-column code="administrator.maintenance-record.list.label.aircraft" path="aircraft" width="25%"/>

</acme:list>