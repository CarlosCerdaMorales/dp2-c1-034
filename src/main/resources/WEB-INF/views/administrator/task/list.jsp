<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.task.list.label.type" path="type" width="33%"/>
	<acme:list-column code="administrator.task.list.label.priority" path="priority" width="33%"/>
	<acme:list-column code="administrator.task.list.label.estimatedDuration" path="estimatedDuration" width="33%"/>

</acme:list>