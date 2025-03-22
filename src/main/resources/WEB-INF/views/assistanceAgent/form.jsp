<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistanceAgent.list.label.claimsresolvedratio" path="claimsResolvedRatio" width="10%"/>
	<acme:list-column code="assistanceAgent.list.label.claimsrejectedratio" path="claimsRejectedRatio" width="10%"/>
</acme:list>

