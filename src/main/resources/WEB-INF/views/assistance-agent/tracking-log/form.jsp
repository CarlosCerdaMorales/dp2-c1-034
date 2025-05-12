<%--
- list	.jsp
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
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode}">
		<acme:input-textbox code="assistance-agent.tracking-log.form.last-update-moment" path="lastUpdateMoment" readonly="true"/>
	</jstl:if>
	<acme:input-textbox code="assistance-agent.tracking-log.form.last-step-undergoing" path="stepUndergoing"/>
 	<acme:input-double code="assistance-agent.tracking-log.form.resolution-percentage" path="resolutionPercentage"/>
	<acme:input-select code="assistance-agent.tracking-log.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-textbox code="assistance-agent.tracking-log.form.label.resolution" path="resolution"/>
	
<jstl:choose>
	<jstl:when test="${_command == 'create'}">
		<acme:input-checkbox code="assistance-agent.claim.form.label.confirmation" path="confirmation"/>
		<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/tracking-log/create?masterId=${claim.id}"/>
	</jstl:when>	
			<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode}">
			<acme:input-checkbox code="assistance-agent.claim.form.label.confirmation" path="confirmation"/>
			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/tracking-log/update"/>
			<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
			<acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
	</jstl:when>
	
</jstl:choose>
</acme:form>
