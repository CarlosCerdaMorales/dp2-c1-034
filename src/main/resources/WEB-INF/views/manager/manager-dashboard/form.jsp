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
	<acme:input-textarea code="customer.customer-dashboard.form.label.lastFiveDestinations" path="lastFiveDestinations"/>
	<acme:input-double code="customer.customer-dashboard.form.label.moneySpentInBookingsLastYear" path="moneySpentInBookingsLastYear"/>
	<acme:input-textbox code="customer.customer-dashboard.form.label.bookingsGroupedByTravelClass" path="bookingsGroupedByTravelClass"/>
	<acme:input-textbox code="customer.customer-dashboard.form.label.costsOfBookingsLastFiveYears" path="costsOfBookingsLastFiveYears"/>
	<acme:input-textbox code="customer.customer-dashboard.form.label.numberOfPassengersInBookings" path="numberOfPassengersInBookings"/>
</acme:form>