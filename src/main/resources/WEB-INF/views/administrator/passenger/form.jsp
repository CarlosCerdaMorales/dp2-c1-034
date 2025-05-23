<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.passenger.form.fullName" path="fullName"/>
	<acme:input-textbox code="administrator.passenger.form.email" path="email"/>
	<acme:input-textbox code="administrator.passenger.form.passport" path="passport"/>	
	<acme:input-moment code="administrator.passenger.form.dateOfBirth" path="dateOfBirth"/>
	<acme:input-textbox code="administrator.passenger.form.specialNeeds" path="specialNeeds"/>
</acme:form>
