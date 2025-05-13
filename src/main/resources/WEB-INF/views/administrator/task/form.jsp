<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.task.form.label.type" path="type"/>
	<acme:input-textbox code="administrator.task.form.label.description" path="description"/>	
	<acme:input-textbox code="administrator.task.form.label.priority" path="priority"/>	
	<acme:input-textbox code="administrator.task.form.label.estimatedDuration" path="estimatedDuration"/>
</acme:form>