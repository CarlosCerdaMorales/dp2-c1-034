<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textarea code="assistance-agent.form.label.description" path="description"/>
	<acme:input-textbox code="assistance-agent.form.label.passenger-email" path="passengerEmail"/>
	<acme:input-select code="assistance-agent.form.label.claim-type" path="claimType" choices="${claimTypes}"/>
	<jstl:if  test="${acme:anyOf(_command, 'show|update|publish') }">
		<acme:input-textarea code="assistance-agent.form.label.status" path="status" readonly="true"/>
	</jstl:if>
	
	
	<acme:input-select code="assistance-agent.form.label.leg" path="leg" choices="${legs}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode}">
			<acme:input-checkbox code="assistance-agent.claim.form.label.confirmation" path="confirmation"/>
			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
			<acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="assistance-agent.claim.form.label.confirmation" path="confirmation"/>
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>	
			<jstl:when test="${_command == 'show'}">
			<acme:button code="assistance-agent.claim.form.button.tracking-log" action="/assistance-agent/tracking-log/list?masterId=${id}"/>			
		</jstl:when>	
	</jstl:choose>
</acme:form>

