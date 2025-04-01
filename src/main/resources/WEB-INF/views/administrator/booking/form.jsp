<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.booking.form.locatorCode" path="locatorCode"/>
	<acme:input-moment code="administrator.booking.form.purchaseMoment" path="purchaseMoment" readonly="true"/>
	<acme:input-select code="administrator.booking.form.travelClass" path="travelClass" choices="${classes}"/>	
	<acme:input-money code="administrator.booking.form.price" path="price" readonly="true"/>
	<acme:input-textbox code="administrator.booking.form.lastNibble" path="lastNibble"/>
	<acme:input-select code="administrator.booking.form.flight" path="flight" choices="${flights}"/>	
	<acme:input-textbox code="administrator.booking.form.customer" path="customer"/>

	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="customer.booking.form.show.passengers" action="/administrator/passenger/list?bookingId=${bookingId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
