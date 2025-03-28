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
	<acme:list-column code="flight-crew-member.flight-assignment-uncomplete.list.label.flightCrewDuty" path="flightCrewDuty" width="20%"/>
	<acme:list-column code="flight-crew-member.flight-assignment-uncomplete.list.label.lastUpdate" path="lastUpdate" width="20%"/>
	<acme:list-column code="flight-crew-member.flight-assignment-uncomplete.list.label.assignmentStatus" path="assignmentStatus" width="20%"/>
	<acme:list-payload path="payload"/>	
</acme:list>

<jstl:if test="${_command == 'listUncomplete'}">
	<acme:button code="flightCrewMember.flightAssignment.list.button.create" action="/flightCrewMember/flightAssignment/create"/>
</jstl:if>